package base;

import java.util.function.Supplier;
import java.util.UUID;

public final class Test {
  public static Integer a = 0;

  public String toString() {
    return "hi";
  }

  public static void main(String[] args) {
    final Integer b = a;
    final Supplier<Integer> sA = () -> a;
    final Supplier<Integer> sB = () -> b;
    System.out.println("a = %s".formatted(sA.get()));
    System.out.println("b = %s".formatted(sB.get()));

    System.out.println("setting a...");
    a = 1;

    System.out.println("a = %s".formatted(sA.get()));
    System.out.println("b = %s".formatted(sB.get()));

    System.out.println("b = %s".formatted(UUID.randomUUID()));

    final Test t = new Test();
    System.out.println("%s".formatted(((Object) t).toString()));
  }
}
