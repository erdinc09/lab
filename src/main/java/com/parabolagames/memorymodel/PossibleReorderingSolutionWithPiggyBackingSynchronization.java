package com.parabolagames.memorymodel;


// modified from Java Concurrency In Practice "Great Book!!!!"
public class PossibleReorderingSolutionWithPiggyBackingSynchronization {
  static int x = 0, y = 0;
  static int a = 0;
  static int b = 0;

  public static void main(String[] args) throws InterruptedException {

    while (true) {
      Thread one =
          new Thread(
              () -> {
                a = 1;
                System.out.println(
                    "thread one"); // <-- println deep inside synchronizes on System.out object
                // since two thread synchronizes on the same object, now happens-before relation
                // holds!
                x = b;
              });
      Thread other =
          new Thread(
              () -> {
                b = 1;
                System.out.println(
                    "thread other"); // <-- println deep inside synchronizes on System.out object
                // since two thread synchronizes on the same object, now happens-before relation
                // holds!
                y = a;
              });
      one.start();
      other.start();
      one.join();
      other.join();

      if (x == 0 && y == 0) {
        // ( x=0, y=0) impossible! loop forever
        System.out.println("( x=" + x + ", y=" + y + ")");
        break;
      }

      x = 0;
      y = 0;
      a = 0;
      b = 0;
    }
  }
}
