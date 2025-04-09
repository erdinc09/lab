package com.parabolagames.generics;

import java.util.concurrent.ThreadLocalRandom;

public class GenericVarArgsHelper {
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
}
