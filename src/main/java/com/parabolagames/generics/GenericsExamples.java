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

        /*
        At heart, these terms describe how the subtype relation is affected by type transformations.

        That is, if A and B are types, f is a type transformation,
        and ≤ the subtype relation (i.e. A ≤ B means that A is a subtype of B), we have

        1 - f is covariant if A ≤ B implies that f(A) ≤ f(B)
        2 - f is contravariant if A ≤ B implies that f(B) ≤ f(A)
        3 - f is invariant if neither of the above holds

         */


        /* Producer, Covariance*:Covariance is about producers*/
        // List of apples
        List<Apple> apples = new ArrayList<>();
        apples.add(new Apple());

        // We can assign a list of apples to a basket of fruits;
        // because apple is subtype of fruit
        List<? extends Fruit> basket = apples;//since  Apple ≤ "? extends Fruit",
        // (Apple subtype, with covariance relation also holds for therefore
        // we can assign "List<Apple>" to "List<? extends Fruit>" ) like we assign Apple to Fruit, same direction!

        // Here we know that in basket there is nothing but fruit only
        for (Fruit fruit : basket) {
            System.out.println(fruit);
        }

        // However we can not be sure about the compile type of "basket".
        // To preserve type safety, putting anything to basket is forbidden.
        // basket.add(new Apple()); //Compile time error
        // basket.add(new Fruit()); //Compile time error


        /* Consumer, Contravariance: Contravariance is about consumers**/

        // We can assign a list of apples to a basket of apples
        List<? super Apple> basket2 = apples; // since  "Apple" ≤ "? super Apple", and with "? super X" means contravariance in java
        // Then by the definition, subtyping relation must be reversed: "List<? super Apple>" ≤ "List<Apple>". Accordingly, (I can assign
        // subtype to super type) I can assign "List<Apple>" to "List<? super Apple>"!

        //compile time error
        //apples = basket2;

        // Moreover, we can be sure that, compile time type of "List<? super Apple>" has to be "List<Object>" or "List<Fruit>".
        // There is no other option. This enables us to put something in the container without breaking the type safety"
        // Anything that is subtype of "Apple" because of the compile type of it.
        List<? super Apple> basket3 = List./*<Object>*/of(new Object(), new Object()); // also possible
        List<? super Apple> basket4 = List./*<Object>*/of("abv", "ddd"); // also possible!
        List<? super Apple> basket5 = List.<Fruit>of(new Fruit()); // also possible!
        //List<? super Apple> basket5 =  List.<String>of("abv","ddd"); // not possible!, compile error

        basket2.add(new Apple()); // Successful
        basket2.add(new AsianApple()); // Successful
        // basket2.add(new Fruit());      //Compile time error
        //basket2.add(new Object());      //Compile time error
        //basket2.add("asdsad");      //Compile time error


        /*
        //Compile time error
        for (Apple apple : basket2) {
            System.out.println(apples);
        }


        // Compile time error
        for (Fruit fruit : basket2) {
            System.out.println(apples);
        }
        */

        for (Object object : basket2) {
            System.out.println(object);
        }

    }
}
