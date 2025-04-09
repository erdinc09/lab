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




    // class [Ljava.lang.Object; cannot be cast to class [Ljava.lang.String; ([Ljava.lang.Object;and
    // [Ljava.lang.String; are in module java.base of loader 'bootstrap')
    //	at com.parabolagames.generics.GenericsVarArgs.main(GenericsVarArgs.java:8)


    //belows are compiling since the new arrays are created calling side, where the compiler now the real type.
    String[] strings1 = toArray("Good", "Fast", "Cheap");
    String[] strings2 = GenericVarArgsHelper.toArray("Good", "Fast", "Cheap");//proof for when the varargs in another file but anyway, array creation is here
    Integer[] integers = GenericVarArgsHelper.toArray(1,2, 3);
    System.out.println("strings1: " +  Arrays.toString(strings1));
    System.out.println("strings2: " +  Arrays.toString(strings2));
    System.out.println("integers: " +  Arrays.toString(integers));


    //below however throw class cast exception.
    // Reason is that, since toArray is called within method "pickTwo", new array created for varargs is type of Object[]
    //please see the of disassemble code of method "static <T> T[] pickTwo(T, T, T)" (byte code)
    // at line 33 --> "33: anewarray     #20                 // class java/lang/Object"

    //Always keep in mind that, in java there is only one compilation unit for generics, unlike C++!.
    String[] attributes = pickTwo("Good", "Fast", "Cheap");
    /*



     static <T> T[] pickTwo(T, T, T);
    Code:
       0: invokestatic  #18                 // Method java/util/concurrent/ThreadLocalRandom.current:()Ljava/util/concurrent/ThreadLocalRandom;
       3: iconst_3
       4: invokevirtual #19                 // Method java/util/concurrent/ThreadLocalRandom.nextInt:(I)I
       7: tableswitch   { // 0 to 2
                     0: 32
                     1: 48
                     2: 64
               default: 80
          }
      32: iconst_2
      33: anewarray     #20                 // class java/lang/Object
      36: dup
      37: iconst_0
      38: aload_0
      39: aastore
      40: dup
      41: iconst_1
      42: aload_1
      43: aastore
      44: invokestatic  #6                  // Method toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
      47: areturn
      48: iconst_2
      49: anewarray     #20                 // class java/lang/Object
      52: dup
      53: iconst_0
      54: aload_0
      55: aastore
      56: dup
      57: iconst_1
      58: aload_2
      59: aastore
      60: invokestatic  #6                  // Method toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
      63: areturn
      64: iconst_2
      65: anewarray     #20                 // class java/lang/Object
      68: dup
      69: iconst_0
      70: aload_1
      71: aastore
      72: dup
      73: iconst_1
      74: aload_2
      75: aastore
      76: invokestatic  #6                  // Method toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
      79: areturn
      80: new           #21                 // class java/lang/AssertionError
      83: dup
      84: invokespecial #22                 // Method java/lang/AssertionError."<init>":()V
      87: athrow



     */
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
