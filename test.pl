use strict;
use POSIX;

my $dotFilePrefix = "/test//";
print($dotFilePrefix . "\n");
$dotFilePrefix =~ s/\/*$//;
print($dotFilePrefix . "\n");
