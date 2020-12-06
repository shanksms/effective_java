package com.shashank.effective.java.concurrency;

import java.util.concurrent.TimeUnit;

public class StopThread {

  private static volatile boolean stopRequested;

  public static void main(String[] args) throws Exception{

    Thread thread = new Thread( () -> {
        while (!stopRequested) {
          System.out.println("I am running");
        }
      System.out.println("Going to die now:");
    });

    thread.start();
    TimeUnit.SECONDS.sleep(1);
    stopRequested = true;

  }

}
