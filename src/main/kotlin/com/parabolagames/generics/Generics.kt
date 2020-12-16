package com.parabolagames.generics

//https://www.geeksforgeeks.org/kotlin-generics/
//https://kotlinlang.org/docs/reference/generics.html
class OutClass<out T>(val value: T) {
    fun get(): T {
        return value
    }

}

val out = OutClass("string")
val ref: OutClass<Any> = out

class InClass<in T> {
    fun toString(value: T): String {
        return value.toString()
    }
}

val inClassObject: InClass<Number> = InClass()
val ref2:InClass<Int> = inClassObject


fun copy(from: Array<out Any>, to: Array<Any>) {
    assert(from.size == to.size)
    // copying (from) array to (to) array
    for (i in from.indices)
        to[i] = from[i]
    // printing elements of array in which copied
    for (i in to.indices) {
        println(to[i])
    }
}

// star projection in array
fun printArray(array: Array<*>) {
    array.forEach { print(it) }
}

fun main(args :Array<String>) {
    val ints: Array<Int> = arrayOf(1, 2, 3)
    val any :Array<Any> = Array<Any>(3) { "" }
    copy(ints, any)

    val name  = arrayOf("Geeks","for","Geeks")
    printArray(name)
}