package base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Java Memory Model (JMM) allows threads to cache variables locally,
 * and without proper synchronization, updates made by one thread may not
 * be visible to others.
 * <p>
 * Even if all writes are synchronized, reads that are not synchronized:
 * <p>
 * 1. May access stale data from thread-local caches.
 * 2. Are not guaranteed to see the effects of previous writes.
 * 3. Can lead to visibility issues, especially on multi-core systems.
 */
public class UnsynchronizedReads {
  private final ReentrantLock lock;
  private final Map<Integer, Long> map;

  public UnsynchronizedReads() {
    this.lock = new ReentrantLock();
    this.map = new HashMap<>();
  }

  public void add(final int i) {
    this.lock.lock();
    try {
      this.map.put(i, System.currentTimeMillis());
    } finally {
      this.lock.unlock();
    }
  }

  public void remove(final int i) {
    this.lock.lock();
    try {
      this.map.remove(i);
    } finally {
      this.lock.unlock();
    }
  }

  public Long reader(final int i) {
    return this.map.get(i);
  }
}
