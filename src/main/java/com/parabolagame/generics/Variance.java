package com.parabolagame.generics;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Variance {

  static class Animal {}

  static class Dog extends Animal {}

  static class Cat extends Animal {}

  public static void main(String[] args) {
    Dog[] dogs = new Dog[3];
    Cat[] cats = new Cat[3];

    Animal[] animals = dogs;

    animals[0] = new Cat();

    Set<Integer> si = new TreeSet<Integer>();
    Set s = new TreeSet<Integer>();
    Set<String> ss = s; // unchecked warning
    si = s; // possible
    s = si; // possible, interesting

    s.add(new Integer(42)); // another unchecked warning
    Iterator<String> iter = ss.iterator();

    while (iter.hasNext()) {
      String str = iter.next(); // ClassCastException thrown
      System.out.println(str);
    }
  }
}
