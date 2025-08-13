package base;

public class InstanceofNullSafety {

  public static void main(final String args[]) {
    final Object obj = (args.length > 0) ? args[0] : null;

    if (obj instanceof String) {
      System.out.println("'%s' is a string".formatted(obj));
    } else {
      System.out.println("'%s' is NOT a string".formatted(obj));
    }
  }
}
