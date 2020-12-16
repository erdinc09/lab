package com.parabolagames.generics;

import java.util.List;

// from Effective Java 3rd Edition Item 32
public class VarargsHeapPollution {

  static void dangerous(List<String>... stringLists) {
    List<Integer> intList = List.of(42);
    Object[] objects = stringLists;
    objects[0] = intList; // Heap pollution
    String s = stringLists[0].get(0); // ClassCastException
  }
}
