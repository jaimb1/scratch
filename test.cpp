
struct Timer {};

struct TimeKeeper {
  explicit TimeKeeper(Timer t);
  int get_time();
};

int main() {
  TimeKeeper time_keeper(Timer());
  //TimeKeeper time_keeper(Timer t());
  return time_keeper.get_time();
}

