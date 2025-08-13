import json
import html
import sys
from uuid import UUID
from typing import List,Dict

# Define classes
class Revision:
    def __init__(self, version: int, time_stamp: str, hlc_counter: int, client_id: int, revision_type: str):
        self.version = version
        self.time_stamp = time_stamp
        self.hlc_counter = hlc_counter
        self.client_id = client_id
        self.revision_type = revision_type

class Slice:
    def __init__(self, slice_name: str, revisions: List[Revision]):
        self.slice_name = slice_name
        self.revisions = revisions

class Disk:
    def __init__(self,
                 uuid: UUID,
                 corrupted_revisions: List[Slice],
                 revision_error_slices: List[Slice],
                 appeared_slices: List[Slice],
                 disappeared_slices: List[Slice]):
        self.uuid = uuid
        self.corrupted_revisions = corrupted_revisions
        self.revision_error_slices = revision_error_slices
        self.appeared_slices = appeared_slices
        self.disappeared_slices = disappeared_slices

def decode_html_entities(obj):
    if isinstance(obj, str):
        return html.unescape(obj)
    elif isinstance(obj, dict):
        return {k: decode_html_entities(v) for k, v in obj.items()}
    elif isinstance(obj, list):
        return [decode_html_entities(item) for item in obj]
    else:
        return obj

def parse_entry(disk_id: UUID, disk_data):
    corrupted_revisions = disk_data['corrupted_revisions']
    revision_error_slices = disk_data['revision_error_slices']

    appeared_slices = [
        Slice(slice_data['slice_name'], [Revision(**rev) for rev in slice_data['revisions']])
        for slice_data in disk_data['appeared_slices']
    ]
    disappeared_slices = [
        Slice(slice_data['slice_name'], [Revision(**rev) for rev in slice_data['revisions']])
        for slice_data in disk_data['disappeared_slices']
    ]

    return Disk(disk_id, corrupted_revisions, revision_error_slices, appeared_slices, disappeared_slices)


def add_entries(slices_to_disk_ids_map: Dict[str, Dict[str, str]], slice_list: List[Slice], list_type: str, disk_id: UUID):
    for next_slice in slice_list:
        if next_slice.slice_name in slices_to_disk_ids_map:
            slices_to_disk_ids_map[next_slice.slice_name][str(disk_id)] = list_type
        else:
            slices_to_disk_ids_map[next_slice.slice_name] = {str(disk_id): list_type}


def build_slices_to_disks_map(disk_entries: Dict[UUID, Disk]):
    slices_to_disks = {}
    for disk_id, disk in disk_entries.items():
        # add_entries(slices_to_disks, disk.corrupted_revisions, "corrupted_revisions", disk_id)
        # add_entries(slices_to_disks, disk.revision_error_slices, "revision_error_slices", disk_id)
        add_entries(slices_to_disks, disk.appeared_slices, "appeared_slices", disk_id)
        add_entries(slices_to_disks, disk.disappeared_slices, "disappeared_slices", disk_id)
    return slices_to_disks


if __name__ == "__main__":
    json_input = sys.stdin.read()
    data = json.loads(json_input)
    decoded_data = decode_html_entities(data)
    entries = {UUID(key): parse_entry(UUID(key), value) for key, value in decoded_data.items()}

    slice_map = build_slices_to_disks_map(entries)
    encoder = json.encoder.JSONEncoder()
    json_output = encoder.encode(slice_map)
    print(json_output)

