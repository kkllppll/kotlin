package com.example.lw_4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.math.sqrt

class Task1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Прив'язуємо макет
        return inflater.inflate(R.layout.fragment_task1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Знаходимо елементи у макеті
        val inputSm = view.findViewById<EditText>(R.id.inputSm)
        val inputUnom = view.findViewById<EditText>(R.id.inputUnom)
        val inputJek = view.findViewById<EditText>(R.id.inputJek)
        val inputIk = view.findViewById<EditText>(R.id.inputIk)
        val inputTf = view.findViewById<EditText>(R.id.inputTf)
        val inputCt = view.findViewById<EditText>(R.id.inputCt)
        val btnCalculate = view.findViewById<Button>(R.id.btnCalculate)
        val outputResults = view.findViewById<TextView>(R.id.outputResults)

        btnCalculate.setOnClickListener {
            // Отримуємо значення з полів
            val Sm = inputSm.text.toString().toDoubleOrNull() ?: 0.0
            val Unom = inputUnom.text.toString().toDoubleOrNull() ?: 0.0
            val Jek = inputJek.text.toString().toDoubleOrNull() ?: 0.0
            val Ik = inputIk.text.toString().toDoubleOrNull() ?: 0.0
            val Tf = inputTf.text.toString().toDoubleOrNull() ?: 0.0
            val Ct = inputCt.text.toString().toDoubleOrNull() ?: 0.0

            // Розрахунок I нормального
            val Im = (Sm / 2) / (sqrt(3.0) * Unom)


            // Розрахунок I післяаварійного
            val Ipa = 2 * Im

            // Економічний переріз
            val Sek = Im / Jek

            // Переріз для термічної стійкості
            val Smin = (Ik * sqrt(Tf)) / Ct



            // Вивід результату
            outputResults.text = """
                Розрахунковий струм для нормального режиму Iм: ${"%.2f".format(Im)} А
                Розрахунковий струм для післяаварійного режиму Iм.па: ${"%.2f".format(Ipa)} А
                Економічний переріз: ${"%.2f".format(Sek)} мм²
                Переріз для термічної стійкості: ${"%.2f".format(Smin)} мм²
               
            """.trimIndent()
        }
    }
}
