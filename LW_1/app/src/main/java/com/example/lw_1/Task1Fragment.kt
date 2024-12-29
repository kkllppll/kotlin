package com.example.lw_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment


class Task1Fragment : Fragment() {

    private lateinit var calculator: FuelCalculator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // прив'язуємо макет
        return inflater.inflate(R.layout.fragment_task1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        calculator = FuelCalculator() // ініціалізуємо калькулятор

        val btnCalculate = view.findViewById<Button>(R.id.btnCalculate)
        val resultText = view.findViewById<TextView>(R.id.outputResults)


        // вказуємо дії при натисканні на кнопку "Розрахувати"
        btnCalculate.setOnClickListener {
            try {
                // отримуємо значення з EditText полів
                val hydrogen = view.findViewById<EditText>(R.id.editText_HP).text.toString().toDouble()
                val carbon = view.findViewById<EditText>(R.id.editText_CP).text.toString().toDouble()
                val sulfur = view.findViewById<EditText>(R.id.editText_SP).text.toString().toDouble()
                val nitrogen = view.findViewById<EditText>(R.id.editText_NP).text.toString().toDouble()
                val oxygen = view.findViewById<EditText>(R.id.editText_OP).text.toString().toDouble()
                val water = view.findViewById<EditText>(R.id.editText_WP).text.toString().toDouble()
                val ash = view.findViewById<EditText>(R.id.editText_AP).text.toString().toDouble()

                // передаємо значення в калькулятор
                calculator.inputFuelComponents(
                    hydrogen,
                    carbon,
                    sulfur,
                    nitrogen,
                    oxygen,
                    water,
                    ash
                )

                // розрахунок складу сухої та горючої маси
                val (dryComposition, dryCoeff) = calculator.coeffWorkingToDry()
                val (combComposition, combCoeff) = calculator.coeffWorkingToComb()

                // розрахунок теплоти згоряння
                val workingQ = calculator.calculateLowerHeatingValueWorkingMass()
                val dryQ = calculator.calculateLowerHeatingValueDryMass(workingQ)
                val combQ = calculator.calculateLowerHeatingValueCombustibleMass(workingQ)

                // формуємо результат для відображення
                val formattedDryComposition =
                    dryComposition.entries.joinToString(", ") { "${it.key}: %.2f".format(it.value) }
                val formattedCombComposition =
                    combComposition.entries.joinToString(", ") { "${it.key}: %.2f".format(it.value) }

                val result = """
                    Коефіцієнт сухої маси: ${"%.2f".format(dryCoeff)}
                    Склад сухої маси: $formattedDryComposition
                    Коефіцієнт горючої маси: ${"%.2f".format(combCoeff)}
                    Склад горючої маси: $formattedCombComposition
                    Теплота згоряння робочої маси: ${"%.2f".format(workingQ)}
                    Теплота згоряння сухої маси: ${"%.2f".format(dryQ)}
                    Теплота згоряння горючої маси: ${"%.2f".format(combQ)}
                """.trimIndent()


                // відображаємо результат у TextView
                resultText.text = result
                resultText.visibility = View.VISIBLE

            } catch (e: Exception) {
                // обробка помилок
                Toast.makeText(requireContext(), "Помилка: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}










