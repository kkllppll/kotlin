package com.example.lw_2

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

import android.widget.Spinner
import android.widget.EditText
import android.widget.TextView
import android.view.View
import android.widget.Toast


class MainActivity : ComponentActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.fuelTypeSpinner)
        val inputHeatCapacity = findViewById<EditText>(R.id.inputHeatCapacity)
        val inputL = findViewById<EditText>(R.id.inputL)
        val inputAr = findViewById<EditText>(R.id.inputAr)
        val inputG = findViewById<EditText>(R.id.inputG)
        val inputn = findViewById<EditText>(R.id.inputn)
        val inputk = findViewById<EditText>(R.id.inputk)
        val inputB = findViewById<EditText>(R.id.inputB)
        val calculateButton = findViewById<Button>(R.id.buttonCalculate)
        val resultText = findViewById<TextView>(R.id.resultText)

        calculateButton.setOnClickListener {
            try {
                // отримання даних з полів вводу
                val fuelType = spinner.selectedItem.toString()
                val Q_iR = inputHeatCapacity.text.toString().toDoubleOrNull() ?: 0.0
                val L = inputL.text.toString().toDoubleOrNull() ?: 0.0
                val Ar = inputAr.text.toString().toDoubleOrNull() ?: 0.0
                val G = inputG.text.toString().toDoubleOrNull() ?: 0.0
                val n = inputn.text.toString().toDoubleOrNull() ?: 0.0
                val k = inputk.text.toString().toDoubleOrNull() ?: 0.0
                val B = inputB.text.toString().toDoubleOrNull() ?: 0.0

                // створення об'єкта калькулятора
                val calculator = FuelCalculator()
                calculator.inputParameters(Q_iR, L, Ar, G, n, k, B)

                // виконання розрахунків
                val emissionFactor = calculator.calculateEmissionFactor()
                val totalEmission = calculator.calculateTotalEmission()

                // вивід результатів
                resultText.text = """
                    Тип палива: $fuelType
                    Показник емісії: ${"%.2f".format(emissionFactor)} г/ГДж
                    Валовий викид: ${"%.2f".format(totalEmission)} т
                """.trimIndent()
                resultText.visibility = View.VISIBLE
            } catch (e: Exception) {
                Toast.makeText(this, "Помилка введення даних: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}