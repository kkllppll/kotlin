package com.example.lw_5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class Task2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // прив'язуємо макет
        return inflater.inflate(R.layout.fragment_task2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Елементи UI
        val omegaInput: EditText = view.findViewById(R.id.input_omega)
        val tbInput: EditText = view.findViewById(R.id.input_t)
        val pInput: EditText = view.findViewById(R.id.input_p)
        val tInput: EditText = view.findViewById(R.id.input_T)
        val kInput: EditText = view.findViewById(R.id.input_k)
        val price_aInput: EditText = view.findViewById(R.id.input_price_a)
        val price_pInput: EditText = view.findViewById(R.id.input_price_p)
        val calculateButton: Button = view.findViewById(R.id.calculate_button)
        val resultText: TextView = view.findViewById(R.id.result_text)

        calculateButton.setOnClickListener {
            // зчитування даних
            val omega = omegaInput.text.toString().toDoubleOrNull() ?: 0.0
            val tb = tbInput.text.toString().toDoubleOrNull() ?: 0.0
            val p = pInput.text.toString().toDoubleOrNull() ?: 0.0
            val T = tInput.text.toString().toDoubleOrNull() ?: 0.0
            val k = kInput.text.toString().toDoubleOrNull() ?: 0.0
            val price_a = price_aInput.text.toString().toDoubleOrNull() ?: 0.0
            val price_p = price_pInput.text.toString().toDoubleOrNull() ?: 0.0

            // розрахунок математичного сподівання аварійного недовідпущення
            val mWnedA = omega * tb * p * T

            // розрахунок математичного сподівання планового недовідпуску
            val mWnedP = k * p * T

            // математичне сподівання збитків від переривання електропостачання
            val Mzper = (price_a * mWnedA) + (price_p * mWnedP)

            // виведення результатів
            resultText.text = """
                Математичне сподівання аварійного недовідпуску: ${"%.2f".format(mWnedA)} кВт·год
                Математичне сподівання планового недовідпуску: ${"%.2f".format(mWnedP)} кВт·год
                Загальні збитки: ${"%.2f".format(Mzper)} грн
            """.trimIndent()
        }
    }
}
