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


class Task2Fragment : Fragment() {

    private lateinit var calculator: MazutCalculator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // прив'язуємо макет
        return inflater.inflate(R.layout.fragment_task2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val btnCalculate = view.findViewById<Button>(R.id.btnCalculate)
        val resultText = view.findViewById<TextView>(R.id.outputResults)

        calculator = MazutCalculator() // ініціалізуємо калькулятор


        // вказуємо дії при натисканні на кнопку "Розрахувати"
        btnCalculate.setOnClickListener {
            try {
                // отримуємо значення з EditText полів
                val carbon = view.findViewById<EditText>(R.id.editText_CP).text.toString().toDouble()
                val hydrogen = view.findViewById<EditText>(R.id.editText_HP).text.toString().toDouble()
                val oxygen = view.findViewById<EditText>(R.id.editText_OP).text.toString().toDouble()
                val sulfur = view.findViewById<EditText>(R.id.editText_SP).text.toString().toDouble()
                val lowerHeating_Q =
                    view.findViewById<EditText>(R.id.editText_LH_Q).text.toString().toDouble()
                val water = view.findViewById<EditText>(R.id.editText_WP).text.toString().toDouble()
                val ash = view.findViewById<EditText>(R.id.editText_AP).text.toString().toDouble()
                val vanadium = view.findViewById<EditText>(R.id.editText_VP).text.toString().toDouble()

                // передаємо значення в калькулятор
                calculator.inputFuelComponents(
                    carbon,
                    hydrogen,
                    oxygen,
                    sulfur,
                    lowerHeating_Q,
                    water,
                    ash,
                    vanadium
                )


                // розрахунок складу сухої та горючої маси
                val (Composition, coeff) = calculator.CombToWorking()

                // розрахунок теплоти згоряння
                val lower_heating = calculator.calculateWorkingHeatingValue(lowerHeating_Q)

                // формуємо результат для відображення
                val formattedComposition =
                    Composition.entries.joinToString(", ") { "${it.key}: %.2f".format(it.value) }

                val result = """
                    Елементарний склад: $formattedComposition
                    Нижча теплота згоряння робочої маси: ${"%.2f".format(lower_heating)}
                    
                """.trimIndent()


                // відображаємо результат у TextView
                resultText.text = result
                resultText.visibility = View.VISIBLE

            }  catch (e: Exception) {
                // обробка помилок
                Toast.makeText(requireContext(), "Помилка: ${e.message}", Toast.LENGTH_LONG).show()
            }

        }
    }
}









