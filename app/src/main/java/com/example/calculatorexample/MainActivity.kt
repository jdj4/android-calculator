package com.example.calculatorexample

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Late init variables can be defined here and initialized later
    // Like declaring `private TextView workingView;` in Java for example
    private lateinit var workingView: TextView
    private lateinit var resultsView: TextView
    private lateinit var operators: String

    // Global variables within our MainActivity class
    // These boolean variables will be important for determining what we can input in an equation
    private var canAddOperation = false
    private var canAddDecimal = true

    /**
     * onCreate is Android's version of a main method. This is the main
     * entry point, and in MainActivity it is the first block of code that
     * will run when your app is started.
     *
     * In our case this includes all the necessary setup for our calculator.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This links our MainActivity.kt file with activity_main.xml
        setContentView(R.layout.activity_main)

        // findViewById creates a reference to a view withing our UI XML file
        // here we grab the views for our input and result text views and set
        // our lateinit variables
        workingView = findViewById(R.id.working_view)
        resultsView = findViewById(R.id.results_view)
        operators = getString(R.string.operators)

        // AC and backspace buttons
        val allClear = findViewById<Button>(R.id.button_ac)
        val backspace = findViewById<Button>(R.id.button_backspace)

        // Number buttons all have similar functions, so they are stored in an array
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

        // Operator buttons reference array
        val operatorButtons: Array<Button> = arrayOf(
            findViewById(R.id.button_add),
            findViewById(R.id.button_subtract),
            findViewById(R.id.button_multiply),
            findViewById(R.id.button_divide),
            findViewById(R.id.button_mod),
        )
        val equals = findViewById<Button>(R.id.button_equals)

        // setOnClickListener defines a function that determines what clicking a specific
        // view will do, here it defines the behavior of clicking the AC button
        allClear.setOnClickListener {
            canAddOperation = false
            canAddDecimal = true
            workingView.text = ""
            resultsView.text = ""
        }

        // Defining backspace functionality
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
        // Method calls for setting view references stored in the arrays
        setNumberButtonOnClicks(numberButtons)
        setOperatorButtonOnClicks(operatorButtons)

        // Running the EquationParser to get the result of our equation
        equals.setOnClickListener {
            EquationParser.init(workingView, operators)
            resultsView.text = EquationParser.calculate()
        }
    }

    /**
     * Loops over the numberButtons array and sets an onClickListener for each.
     * This is defined as appending the text of the button to the working view.
     * If the button is the decimal point we first check if it can be added, and
     * set the boolean variables accordingly.
     */
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

    /**
     * Loops over the operatorButtons array and sets an onClickListener for each.
     * The MOD operation uses the '%' character for the working view.
     */
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