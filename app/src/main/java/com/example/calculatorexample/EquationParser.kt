package com.example.calculatorexample

import android.widget.TextView

object EquationParser {
    private lateinit var workingText: String
    private lateinit var operators: String

    fun init(workingView: TextView, operators: String) {
        this.workingText = workingView.text.toString()
        this.operators = operators
    }

    fun calculate(): String {
        val equation = infixToPostfix()
        if (equation.isEmpty()) return ""

        val solved = solvePostfix(equation)
        return solved.toString()
    }

    private fun infixToPostfix(): List<String> {
        val infix = getInfix()
        val postfix = mutableListOf<String>()
        val stack = Stack<String>()
        val precedence = mapOf("+" to 1, "-" to 1, "×" to 2, "÷" to 2, "%" to 2)

        infix.forEach { token ->
            when {
                token.all { it.isDigit() || it == '.' } -> postfix.add(token)
                else -> {
                    while (stack.isNotEmpty() &&
                        precedence.getOrDefault(token, 0) <=
                        precedence.getOrDefault(stack.peek(), 0)) {
                        postfix.add(stack.pop() + "")
                    }
                    stack.push(token)
                }
            }
        }
        while (stack.isNotEmpty()) {
            postfix.add(stack.pop() + "")
        }
        return postfix
    }

    private fun getInfix(): List<String> {
        val infix = mutableListOf<String>()
        var num = ""
        for (char in workingText) {
            if (char.isDigit() || char == '.') {
                num += char
            } else {
                if (num.isNotEmpty()) {
                    infix.add(num)
                    num = ""
                }
                infix.add(char + "")
            }
        }
        if (num.isNotEmpty()) {
            infix.add(num)
        }
        return infix
    }

    private fun solvePostfix(equation: List<String>): Double {
        val stack = Stack<Double>()
        equation.forEach {
            if (it in operators) {
                val right = stack.pop()
                val left = stack.pop()
                stack.push(evaluate(right, left, it))
            } else {
                println(it)
                stack.push(it.toDouble())
            }
        }
        return stack.pop()
    }

    private fun evaluate(right: Double, left: Double, operator: String): Double {
        return when (operator) {
            "+" -> left + right
            "-" -> left - right
            "×" -> left * right
            "÷" -> left / right
            "%" -> left % right
            else -> 0.0 // Should never execute
        }
    }
}