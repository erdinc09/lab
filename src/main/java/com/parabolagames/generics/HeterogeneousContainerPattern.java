package com.parabolagames.generics;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// from from Effective Java 3rd Edition Item 33

public class HeterogeneousContainerPattern {

    // Typesafe heterogeneous container pattern - client
    public static void main(String[] args) {
        Favorites f = new Favorites();
        f.putFavorite(String.class, "Java");
        f.putFavorite(Integer.class, 0xcafebabe);
        f.putFavorite(Class.class, Favorites.class);
        String favoriteString = f.getFavorite(String.class);
        int favoriteInteger = f.getFavorite(Integer.class);
        Class<?> favoriteClass = f.getFavorite(Class.class);
        System.out.printf("%s %x %s%n", favoriteString, favoriteInteger, favoriteClass.getName());


        f.putFavorite((Class) Integer.class, "Java");//no compile time error,
    }

    public static class Favorites {
        private final Map<Class<?>, Object> favorites = new HashMap<>();

        public <T> void putFavorite(Class<T> type, T instance) {
            favorites.put(Objects.requireNonNull(type), type.cast(instance)); // type.cast(instance) --> ensure run time type safety --> f.putFavorite((Class)Integer.class, "Java");

        }

        public <T> T getFavorite(Class<T> type) {
            return type.cast(favorites.get(type));
            // The cast method is the dynamic analogue of Java’s cast operator. It simply checks that its
            // argument is an instance of the type represented by the Class object. If so, it returns the
            // argument; otherwise it throws a ClassCastException. We know that the cast invocation in
            // getFavorite won’t throw ClassCastException, assuming the client code compiled cleanly. That
            // is to say, we know that the values in the favorites map always match the types of their
            // keys.
        }
    }
}
