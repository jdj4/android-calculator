package com.example.calculatorexample

import android.widget.TextView

/**
 * Notice the keyword object and not class. This follows the singleton design pattern, meaning
 * there is only ever one instance of EquationParser. You can think of this like using
 * a class with all static methods in java.
 */
object EquationParser {
    private lateinit var workingView: String
    private lateinit var operators: String

    /**
     * This is the dependency injection design pattern. The required workingView and operators
     * are set from the call to init in MainActivity.
     */
    fun init(workingView: TextView, operators: String) {
        this.workingView = workingView.text.toString()
        this.operators = operators
    }

    /**
     * Called in the equals button's setOnClickListener in MainActivity. Uses the following
     * helper methods to calculate the result of the equation with an infix to postfix algorithm
     * so that the results can be more easily calculated.
     */
    fun calculate(): String {
        val equation = infixToPostfix()
        if (equation.isEmpty()) return ""

        val solved = solvePostfix(equation)
        return solved.toString()
    }

    /**
     * Gets an infix expression string into a list for parsing.
     */
    private fun getInfix(): List<String> {
        val infix = mutableListOf<String>()
        var num = ""
        for (char in workingView) {
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

    /**
     * Converts an infix expression (ie, 2 + 2) to a postfix expression (ie, 2 2 +).
     * These sorts of expressions are easier for computers to evaluate.
     */
    private fun infixToPostfix(): List<String> {
        val infix = getInfix()
        val postfix = mutableListOf<String>()
        val stack = Stack<String>()
        val precedence = mapOf("+" to 1, "-" to 1, "×" to 2, "÷" to 2, "%" to 2)

        // For each is a lambda expression that is an alternative to `for (token in infix)`
        infix.forEach { token ->
            when {
                // Lambda to check if everything in the current token string is a digit or decimal,
                // add these to the postfix list
                token.all { it.isDigit() || it == '.' } -> postfix.add(token)

                // Otherwise add operators to the stack, and put the highest precedence operators
                // in the postfix list
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
        // Add any remaining operators to the postfix list
        while (stack.isNotEmpty()) {
            postfix.add(stack.pop() + "")
        }
        return postfix
    }

    /**
     * Evaluate a subexpression of a postfix expression. Takes the left and right value and an
     * operator and solves based on the operator.
     */
    private fun evaluate(right: Double, left: Double, operator: String): Double {
        // This is a when statement, a nice syntactic replacement for cascading if/else if/else
        // chains and switch statements
        return when (operator) {
            "+" -> left + right
            "-" -> left - right
            "×" -> left * right
            "÷" -> left / right
            "%" -> left % right
            else -> 0.0 // Should never execute
        }
    }

    /**
     * Solve the whole postfix expression. Uses a stack to store values. If the current token is
     * not an operator it is added to the stack. If it is then we pop the right value from the top
     * of the stack and the left value afterwards and call the evaluate method. Always returns a
     * double.
     */
    private fun solvePostfix(equation: List<String>): Double {
        val stack = Stack<Double>()
        equation.forEach {
            if (it in operators) {
                val right = stack.pop()
                val left = stack.pop()
                stack.push(evaluate(right, left, it))
            } else {
                stack.push(it.toDouble())
            }
        }
        return stack.pop()
    }
}