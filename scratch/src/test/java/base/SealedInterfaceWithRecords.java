package base;

public class SealedInterfaceWithRecords {
  record Success<T>(T result) implements TaskSubmissionResult {}
  record Rejected(String reason) implements TaskSubmissionResult {}

  public sealed interface TaskSubmissionResult permits Success, Rejected {
  } 

  public static void main(final String args[]) {
    final TaskSubmissionResult result = new Success<Integer>(100);
    switch (result) {
      case Success s -> {
        System.out.println("success: " + s);
      }
      case Rejected r -> {
        System.out.println("rejected: " + r);
      }
    }
  }
}
