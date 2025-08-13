package base;

public class MonkeyAndCoconutsPuzzle {

  private static boolean isSolution(final int candidate) {
    int val = candidate;
    for (int i = 0; i < 5; i++) {
      if (val % 5 != 1) {
        return false;
      }
      val = val - (((val - 1) / 5) + 1);
    }
    return val % 5 == 0;
  }

  public static void main(String[] args) {
    int pileSize = 6;
    while (!isSolution(pileSize)) {
      pileSize += 5;
    }
    System.out.println("Starting Pile Size: " + pileSize);
    System.out.printf("First: %d - %d - 1 = %d%n", pileSize, pileSize / 5, pileSize - (pileSize / 5) - 1);
    pileSize = pileSize - (pileSize / 5) - 1;
    System.out.printf("Second: %d - %d - 1 = %d%n", pileSize, pileSize / 5, pileSize - (pileSize / 5) - 1);
    pileSize = pileSize - (pileSize / 5) - 1;
    System.out.printf("Third: %d - %d - 1 = %d%n", pileSize, pileSize / 5, pileSize - (pileSize / 5) - 1);
    pileSize = pileSize - (pileSize / 5) - 1;
    System.out.printf("Fourth: %d - %d - 1 = %d%n", pileSize, pileSize / 5, pileSize - (pileSize / 5) - 1);
    pileSize = pileSize - (pileSize / 5) - 1;
    System.out.printf("Fifth: %d - %d - 1 = %d%n", pileSize, pileSize / 5, pileSize - (pileSize / 5) - 1);
  }
}
