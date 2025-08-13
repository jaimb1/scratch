package base;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class VirtualThreads {

  final static int N_THREADS = 10_000;

  private static int ioTask() {
    System.out.println("Thread.currentThread() = " + Thread.currentThread());
    try {
      Thread.sleep(1000);
    } catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }
    return 0;
  }

  public static void main(String[] args) {
    try (final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      final List<Callable<Integer>> callables = IntStream.range(0, N_THREADS)
          .<Callable<Integer>>mapToObj(ignore -> VirtualThreads::ioTask)
          .toList();

      try {
        final long start = System.nanoTime();
        executor.invokeAll(callables);
        final long elapsed = System.nanoTime() - start;
        System.out.printf("Elapsed: %s%n", TimeUnit.MILLISECONDS.convert(elapsed, TimeUnit.NANOSECONDS));
      } catch (final InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
