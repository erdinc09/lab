package com.parabolagames.generics;

import java.util.ArrayList;
import java.util.List;

// from:https://www.geeksforgeeks.org/what-is-heap-pollution-in-java-and-how-to-resolve-it/
public class HeapPollutionGeeks {
  public static void main(String[] args) {

    // Creating a List of type String
    List<String> listOfString = new ArrayList<>();
    listOfString.add("Geeksforgeeks");

    // Creating a List of type Integer which holds
    // the reference of a List of type String
    // Here compiler will detect that
    // there is a chance of Heap pollution
    // Compiler will throw an unchecked warning
    // at the compile-time only
    List<Integer> listOfInteger = (List<Integer>) (Object) listOfString;

    // Here we are trying to access
    // firstElement of listOfInteger which holds
    // the reference of a List of type String
    // and trying to store it into
    // one variable of type Integer
    Integer firstElement = listOfInteger.get(0);
    System.out.println(firstElement);
  }
}
