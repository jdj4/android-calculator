package com.example.calculatorexample

/**
 * Stack ADT that comes in handy with the EquationParser.
 */
class Stack<T> {
    private val elements = mutableListOf<T>()

    fun push(element: T) {
        elements.add(element)
    }

    fun pop(): T {
        assert(elements.isNotEmpty()) { "You cannot pop from an empty stack." }
        return elements.removeAt(elements.lastIndex)
    }

    fun peek(): T {
        assert(elements.isNotEmpty()) { "You cannot peek from an empty stack." }
        return elements[elements.lastIndex]
    }

    fun isNotEmpty(): Boolean {
        return elements.isNotEmpty()
    }

    override fun toString(): String {
        return "Stack${elements}"
    }
}