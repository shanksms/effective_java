package com.shashank.effective.java.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimeConcurrentExecution {

  public static void main(String[] args) throws Exception{
    ExecutorService executor = Executors.newFixedThreadPool(5);
    System.out.println(new TimeConcurrentExecution().time(executor, 5, () -> System.out.println("running command")));
    executor.shutdown();

  }

  public long time(Executor executor, int concurrency, Runnable action) throws Exception{

    CountDownLatch ready = new CountDownLatch(concurrency);
    CountDownLatch start = new CountDownLatch(1);
    CountDownLatch done = new CountDownLatch(concurrency);

    for (int i = 0; i < concurrency; i++) {

      executor.execute(() -> {
        ready.countDown();
        try {

          start.await(); // Wait till peers are ready

          action.run();

        } catch (InterruptedException e) {

          Thread.currentThread().interrupt();

        } finally {

          done.countDown();  // Tell timer we're done

        }

      });

    }

    ready.await();     // Wait for all workers to be ready

    long startNanos = System.nanoTime();

    start.countDown(); // And they're off!

    done.await();      // Wait for all workers to finish

    return System.nanoTime() - startNanos;


  }

}
