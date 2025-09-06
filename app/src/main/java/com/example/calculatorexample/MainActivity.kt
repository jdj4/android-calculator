package com.example.calculatorexample

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var workingView: TextView
    private lateinit var resultsView: TextView
    private lateinit var operators: String

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workingView = findViewById(R.id.working_view)
        resultsView = findViewById(R.id.results_view)
        operators = getString(R.string.operators)

        // AC and backspace buttons
        val allClear = findViewById<Button>(R.id.button_ac)
        val backspace = findViewById<Button>(R.id.button_backspace)

        // Number buttons
        val numberButtons: Array<Button> = arrayOf(
            findViewById(R.id.button_0),
            findViewById(R.id.button_1),
            findViewById(R.id.button_2),
            findViewById(R.id.button_3),
            findViewById(R.id.button_4),
            findViewById(R.id.button_5),
            findViewById(R.id.button_6),
            findViewById(R.id.button_7),
            findViewById(R.id.button_8),
            findViewById(R.id.button_9),
            findViewById(R.id.button_decimal),
        )

        // Operator buttons
        val operatorButtons: Array<Button> = arrayOf(
            findViewById(R.id.button_add),
            findViewById(R.id.button_subtract),
            findViewById(R.id.button_multiply),
            findViewById(R.id.button_divide),
            findViewById(R.id.button_mod),
        )
        val equals = findViewById<Button>(R.id.button_equals)

        allClear.setOnClickListener {
            canAddOperation = false
            canAddDecimal = true
            workingView.text = ""
            resultsView.text = ""
        }

        backspace.setOnClickListener {
            val text = workingView.text
            if (text.isNotEmpty()) {
                val length = workingView.text.length
                val lastChar = text[length - 1]
                if (length > 0) {
                    if (lastChar in operators)
                        canAddOperation = true
                    workingView.text = text.substring(0, length - 1)
                }
            }
        }
        setNumberButtonOnClicks(numberButtons)
        setOperatorButtonOnClicks(operatorButtons)

        equals.setOnClickListener {
            EquationParser.init(workingView, operators)
            resultsView.text = EquationParser.calculate()
        }
    }

    private fun setNumberButtonOnClicks(numberButtons: Array<Button>) {
        for (button in numberButtons) {
            button.setOnClickListener {
                if (button.text == ".") {
                    if (canAddDecimal)
                        workingView.append(".")
                    canAddDecimal = false
                } else {
                    workingView.append(button.text)
                }
                canAddOperation = true
            }
        }
    }

    private fun setOperatorButtonOnClicks(operatorButtons: Array<Button>) {
        for (button in operatorButtons) {
            button.setOnClickListener {
                if (canAddOperation) {
                    if (button.text == getString(R.string.mod))
                        workingView.append("%")
                    else
                        workingView.append(button.text)
                    canAddOperation = false
                    canAddDecimal = true
                }
            }
        }
    }
}