package com.example.lw_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.TextView

import android.view.View



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Зв'язуємо елементи з XML
        val inputPower = findViewById<EditText>(R.id.inputAveragePower) // середня потужність
        val inputDeviation = findViewById<EditText>(R.id.inputStdDeviation) // стандартне відхилення
        val inputLwrDeviation = findViewById<TextView>(R.id.inputLwrDeviation) // зменшена похибка
        val inputCost = findViewById<EditText>(R.id.inputEnergyCost) // тариф
        val calculateButton = findViewById<Button>(R.id.calculateButton) // кнопка "Розрахувати"
        val outputResult = findViewById<TextView>(R.id.outputResult) // текстове поле для результатів





        calculateButton.setOnClickListener {
            try {
                // зчитуємо введені значення
                val power = inputPower.text.toString().toDouble()
                val deviation = inputDeviation.text.toString().toDouble()
                val lwrdeviation = inputLwrDeviation.text.toString().toDouble()
                val cost = inputCost.text.toString().toDouble()


                // ініціалізуємо Calculator
                val calculator = Calculator()
                calculator.inputParameters(power, deviation, B = cost, sigma_L_P = lwrdeviation)

                // виконуємо розрахунки
                val results = calculator.calculate()

                // форматований вивід результату
                outputResult.text = """
                    Результати розрахунку:

                    До вдосконалення:
                    Частка енергії без небалансів: ${"%.2f".format(results["delta_W1"])} %
                    Згенерована енергія: ${"%.2f".format(results["W1"])} МВт·год
                    Прибуток: ${"%.2f".format(results["profit1"])} тис. грн
                    Залишок енергії: ${"%.2f".format(results["W_penalty1"])} МВт·год
                    Штраф: ${"%.2f".format(results["penalty1"])} тис. грн
                    Чистий прибуток: ${"%.2f".format(results["netProfit1"])} тис. грн

                    Після вдосконалення:
                    Частка енергії без небалансів: ${"%.2f".format(results["delta_W2"])} %
                    Згенерована енергія: ${"%.2f".format(results["W2"])} МВт·год
                    Прибуток: ${"%.2f".format(results["profit2"])} тис. грн
                    Залишок енергії: ${"%.2f".format(results["W_penalty2"])} МВт·год
                    Штраф: ${"%.2f".format(results["penalty2"])} тис. грн
                    Чистий прибуток: ${"%.2f".format(results["netProfit2"])} тис. грн
                """.trimIndent()

                outputResult.visibility = View.VISIBLE
            } catch (e: Exception) {
                // виведення помилки у випадку некоректного введення
                Toast.makeText(this, "Помилка введення даних: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}