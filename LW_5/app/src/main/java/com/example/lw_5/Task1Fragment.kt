package com.example.lw_5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt


class Task1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // прив'язуємо макет
        return inflater.inflate(R.layout.fragment_task1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // елементи ui
        val elementsContainer: LinearLayout = view.findViewById(R.id.elements_container)
        val addElementButton: Button = view.findViewById(R.id.add_element_button)
        val calculateButton: Button = view.findViewById(R.id.calculate_button)
        val coefSimpleDowntimeField: EditText = view.findViewById(R.id.coef_simple_downtime)
        val resultsText: TextView = view.findViewById(R.id.results_text)

        // додавання нового елемента
        addElementButton.setOnClickListener {
            val elementLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            val omegaField = EditText(requireContext()).apply {
                hint = "ω, рік^-1"
                inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val recoveryTimeField = EditText(requireContext()).apply {
                hint = "tВ, год."
                inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val deleteButton = Button(requireContext()).apply {
                text = "Видалити"
                setOnClickListener { elementsContainer.removeView(elementLayout) }
            }

            elementLayout.addView(omegaField)
            elementLayout.addView(recoveryTimeField)
            elementLayout.addView(deleteButton)
            elementsContainer.addView(elementLayout)
        }

        // розрахунок
        calculateButton.setOnClickListener {
            val omegaValues = mutableListOf<Double>()
            val recoveryTimes = mutableListOf<Double>()

            // зчитування даних
            for (i in 0 until elementsContainer.childCount) {
                val elementLayout = elementsContainer.getChildAt(i) as LinearLayout
                val omegaField = elementLayout.getChildAt(0) as EditText
                val recoveryTimeField = elementLayout.getChildAt(1) as EditText

                val omega = omegaField.text.toString().toDoubleOrNull() ?: 0.0
                val recoveryTime = recoveryTimeField.text.toString().toDoubleOrNull() ?: 0.0

                omegaValues.add(omega)
                recoveryTimes.add(recoveryTime)
            }

            // розрахунок частоти відмов
            val omegaSystem = omegaValues.sum()

            // середня тривалість відновлення
            val avgRecoveryTime = if (omegaSystem > 0) {
                val result = omegaValues.zip(recoveryTimes).sumOf { it.first * it.second } / omegaSystem
                (result * 10).roundToInt() / 10.0
            } else {
                0.0
            }

            // зчитування коефіцієнта планового простою
            val kPlMax = coefSimpleDowntimeField.text.toString().toDoubleOrNull() ?: 0.0

            // коефіцієнт аварійного простою
            val kAoc = (omegaSystem * avgRecoveryTime) / 8760

            // коефіцієнт планового простою
            val kPlOc = (1.2 * kPlMax) / 8760

            // частота відмов двоколової системи (без секційного вимикача)
            val omegaDk = 2 * omegaSystem * (kAoc + kPlOc)

            // частота відмов двоколової системи (з урахуванням секційного вимикача)
            val omegaSwitch = 0.02 // частота відмов секційного вимикача
            val omegaDs = omegaDk + omegaSwitch

            // виведення результатів
            resultsText.text = """
                Частота відмов одноколової системи: $omegaSystem рік^-1
                Середній час відновлення: $avgRecoveryTime год.
                Коефіцієнт аварійного простою: %.4e
                Коефіцієнт планового простою: %.4e
                Частота відмов двоколової системи: %.4e рік^-1
                Частота відмов двоколової системи з урахуванням секційного вимикача: %.4e рік^-1
            """.trimIndent().format(kAoc, kPlOc, omegaDk, omegaDs)
        }
    }
}
