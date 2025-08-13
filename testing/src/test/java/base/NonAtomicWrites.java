package base;

import org.openjdk.jcstress.Main;
import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.J_Result;
import org.openjdk.jcstress.util.MutableLong;

@State
@JCStressTest
// read before write
@Outcome(id = "0", expect = Expect.ACCEPTABLE)
// Long.MIN_VALUE + 1
@Outcome(id = "-9223372036854775807", expect = Expect.ACCEPTABLE)
// Long.MIN_VALUE
@Outcome(id = "-9223372036854775808", expect = Expect.FORBIDDEN)
@Outcome(id = "1", expect = Expect.FORBIDDEN)
public class NonAtomicWrites {
  // 1000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0001
  private static final long newValue = Long.MIN_VALUE + 1;

  private long shared = 0L;

  public static void main(final String[] args) throws Exception {
    Main.main(new String[]{"-t", NonAtomicWrites.class.getName()});
  }

  @Actor
  public void writer() {
    this.shared = newValue;
  }

  @Actor
  public void reader(final J_Result result) {
    result.r1 = this.shared;
  }
}
