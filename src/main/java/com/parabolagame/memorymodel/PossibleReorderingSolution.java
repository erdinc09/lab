package com.parabolagame.memorymodel;


//modified from Java Concurrency In Practice "Great Book!!!!"
public class PossibleReorderingSolution {
  static int x = 0, y = 0;
  static volatile int a = 0;
  static volatile int b = 0;

  public static void main(String[] args) throws InterruptedException {

    while (true) {
      Thread one =
          new Thread(
              () -> {
                a = 1;
                x = b;
              });
      Thread other =
          new Thread(
              () -> {
                b = 1;
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
