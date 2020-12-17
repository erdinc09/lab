package com.parabolagames.generics;

import java.util.Collection;
// from from Effective Java 3rd Edition Item 31

// PECS stands for producer-extends, consumer-super.
public class Stack<E> {
  public Stack() {}

  public void push(E e) {}

  public E pop() {
    return null;
  }

  public boolean isEmpty() {
    return false;
  }

  // Wildcard type for parameter that serves as an E consumer
  public void popAll(Collection<? super E> dst) {
    while (!isEmpty()) dst.add(pop());
  }

  // Wildcard type for a parameter that serves as an E producer
  public void pushAll(Iterable<? extends E> src) {
    for (E e : src) push(e);
  }
}
