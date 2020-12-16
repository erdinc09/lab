package com.parabolagames.generics;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

// from https://en.wikipedia.org/wiki/Heap_pollution
public class HeapPollutionDemo {
  public static void main(String[] args) {
    Set s = new TreeSet<Integer>();
    Set<String> ss = s; // unchecked warning
    s.add(42); // another unchecked warning
    Iterator<String> iter = ss.iterator();

    while (iter.hasNext()) {
      String str = iter.next(); // ClassCastException thrown
      System.out.println(str);
    }
  }
}
