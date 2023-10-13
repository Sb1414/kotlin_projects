package com.example.sbcalc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var currentInput = ""
    private var currentResult = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("SetTextI18n")
    fun onNumberClick(view: View) {
        if (view is Button) {
            currentInput += view.text
            updateWorkingTextView()
        }
    }

    fun onOperatorClick(view: View) {
        if (view is Button) {
            currentInput += " " + view.text + " "
            updateWorkingTextView()
        }
    }

    fun onClearClick(view: View) {
        currentInput = ""
        currentResult = ""
        updateWorkingTextView()
        updateResultsTextView()
    }

    fun onBackspaceClick(view: View) {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length - 1)
            updateWorkingTextView()
        }
    }

    fun onDotClick(view: View) {
        if (currentInput.isEmpty()) {
            currentInput = "0."
        } else {
            val lastChar = currentInput.last()
            if (lastChar.isDigit() && !currentInput.substringAfterLast(" ").contains(".")) {
                currentInput += "."
            } else if (lastChar == ' ') {
                currentInput += "0."
            }
        }
        updateWorkingTextView()
    }

    fun onEqualsClick(view: View) {
        try {
            val result = evaluateExpression(currentInput)
            currentResult = result.toString()
            updateResultsTextView()
        } catch (e: Exception) {
            currentResult = "Error"
            updateResultsTextView()
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val tokens = expression.split(" ")
        val stack = mutableListOf<Double>()
        val operators = mutableListOf<String>()

        for (token in tokens) {
            when {
                token.matches(Regex("-?\\d+(\\.\\d+)?")) -> stack.add(token.toDouble())
                token in setOf("+", "-", "x", "/") -> {
                    while (operators.isNotEmpty() && hasPrecedence(operators.last(), token)) {
                        applyOperator(stack, operators.removeAt(operators.size - 1))
                    }
                    operators.add(token)
                }
                token == "(" -> operators.add(token)
                token == ")" -> {
                    while (operators.isNotEmpty() && operators.last() != "(") {
                        applyOperator(stack, operators.removeAt(operators.size - 1))
                    }
                    operators.removeAt(operators.size - 1)
                }
            }
        }

        while (operators.isNotEmpty()) {
            applyOperator(stack, operators.removeAt(operators.size - 1))
        }

        return stack[0]
    }

    private fun hasPrecedence(op1: String, op2: String): Boolean {
        val precedence = mapOf("+" to 1, "-" to 1, "x" to 2, "/" to 2)
        return precedence[op1] ?: 0 >= precedence[op2] ?: 0
    }

    private fun applyOperator(stack: MutableList<Double>, operator: String) {
        val b = stack.removeAt(stack.size - 1)
        val a = stack.removeAt(stack.size - 1)
        when (operator) {
            "+" -> stack.add(a + b)
            "-" -> stack.add(a - b)
            "x" -> stack.add(a * b)
            "/" -> stack.add(a / b)
        }
    }

    private fun updateWorkingTextView() {
        val workingsTV = findViewById<TextView>(R.id.workingsTV)
        workingsTV.text = currentInput
    }

    private fun updateResultsTextView() {
        val resultsTV = findViewById<TextView>(R.id.resultsTV)
        resultsTV.text = currentResult
    }


}