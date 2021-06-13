package com.parabolagames.generics;

/**
 * source:https://howtodoinjava.com/java/generics/java-generics-what-is-pecs-producer-extends-consumer-super/
 */

import java.util.ArrayList;
import java.util.List;

class Fruit {
  @Override
  public String toString() {
    return "I am a Fruit !!";
  }
}

class Apple extends Fruit {
  @Override
  public String toString() {
    return "I am an Apple !!";
  }
}

class AsianApple extends Apple {
  @Override
  public String toString() {
    return "I am an AsianApple !!";
  }
}

public class GenericsExamples {
  public static void main(String[] args) {

    /** Producer* */
    // List of apples
    List<Apple> apples = new ArrayList<>();
    apples.add(new Apple());

    // We can assign a list of apples to a basket of fruits;
    // because apple is subtype of fruit
    List<? extends Fruit> basket = apples;

    // Here we know that in basket there is nothing but fruit only
    for (Fruit fruit : basket) {
      System.out.println(fruit);
    }

    // basket.add(new Apple()); //Compile time error
    // basket.add(new Fruit()); //Compile time error

    // List of apples
    List<Apple> apples2 = new ArrayList<>();
    apples2.add(new Apple());

    // We can assign a list of apples to a basket of apples
    List<? super Apple> basket2 = apples;

    basket2.add(new Apple()); // Successful
    basket2.add(new AsianApple()); // Successful
    // basket2.add(new Fruit());      //Compile time error

    // Compile time error
    //        for (Apple apple : basket2)
    //        {
    //            System.out.println(apples);
    //        }
  }
}
