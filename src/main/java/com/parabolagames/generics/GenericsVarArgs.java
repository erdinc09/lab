package com.parabolagames.generics;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// from from Effective Java 3rd Edition Item 32

// This example is meant to drive home the point that it is unsafe to give another method access to
// a generic varargs parameter array, with two exceptions: it is safe to pass the array to another
// varargs method that is correctly annotated with @SafeVarargs, and it is safe to pass the array to
// a non-varargs method that merely computes some function of the contents of the array.

// The rule for deciding when to use the SafeVarargs annotation is simple: Use @SafeVarargs on every
// method with a varargs parameter of a generic or parameterized type, so its users won’t be
// burdened by needless and confusing compiler warnings. This implies that you should never write
// unsafe varargs methods like dangerous or toArray. Every time the compiler warns you of possible
// heap pollution from a generic varargs parameter in a method you control, check that the method is
// safe. As a reminder, a generic varargs methods is safe if:
//
// 1. it doesn’t store anything in the varargs parameter array, and
// 2. it doesn’t make the array (or a clone) visible to untrusted code. If either of these
// prohibitions is violated, fix it.
public class GenericsVarArgs {

  public static void main(String[] args) {
    String[] attributes = pickTwo("Good", "Fast", "Cheap");
    // class [Ljava.lang.Object; cannot be cast to class [Ljava.lang.String; ([Ljava.lang.Object;and
    // [Ljava.lang.String; are in module java.base of loader 'bootstrap')
    //	at com.parabolagames.generics.GenericsVarArgs.main(GenericsVarArgs.java:8)
  }

  static <T> T[] toArray(T... args) {
    // System.out.println(Arrays.asList(args)); // it is safe
    return args;
  }

  static <T> T[] pickTwo(T a, T b, T c) {
    switch (ThreadLocalRandom.current().nextInt(3)) {
      case 0:
        return toArray(a, b);
      case 1:
        return toArray(a, c);
      case 2:
        return toArray(b, c);
    }
    throw new AssertionError(); // Can't get here
  }

  //The resulting code is typesafe because it uses only generics, and not arrays.
  static <T> List<T> pickTwoRightWay(T a, T b, T c) {
    switch (ThreadLocalRandom.current().nextInt(3)) {
      case 0:
        return List.of(a, b);
      case 1:
        return List.of(a, c);
      case 2:
        return List.of(b, c);
    }
    throw new AssertionError();
  }
}
