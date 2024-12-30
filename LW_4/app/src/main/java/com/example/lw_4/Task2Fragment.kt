package com.example.lw_4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlin.math.pow
import com.example.lw_4.R
import kotlin.math.sqrt
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class Task2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task2, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Знаходимо елементи у макеті
        val inputUsn = view.findViewById<EditText>(R.id.inputUsn)
        val inputSk = view.findViewById<EditText>(R.id.inputSk)
        val inputUk = view.findViewById<EditText>(R.id.inputUk)
        val inputSnom = view.findViewById<EditText>(R.id.inputSnom)
        val btnCalculate = view.findViewById<Button>(R.id.btnCalculate)
        val outputResults = view.findViewById<TextView>(R.id.outputResults)

        btnCalculate.setOnClickListener {
            // Отримуємо значення з полів
            val Usn = inputUsn.text.toString().toDoubleOrNull() ?: 0.0
            val Sk = inputSk.text.toString().toDoubleOrNull() ?: 0.0
            val Uk = inputUk.text.toString().toDoubleOrNull() ?: 0.0
            val Snom = inputSnom.text.toString().toDoubleOrNull() ?: 0.0


            // Розрахунок опори елементів заступної системи

            val Xc = Math.pow(Usn, 2.0) / Sk
            val Xt = Uk /  100 * Math.pow(Usn, 2.0) / Snom


            // Сумарний опір для точки К1

            val Xsum = Xc + Xt

            // Пачаткове діюче знаечння струму трифазного КЗ

            val Ip0 = Usn / (sqrt(3.0) * Xsum)



            // Вивід результату
            outputResults.text = """
                Опір елементів заступної системи Xc: ${"%.2f".format(Xc)} Ом
                Опір елементів заступної системи Xt: ${"%.2f".format(Xt)} Ом
                Сумарний опір для точки К1: ${"%.2f".format(Xsum)} Ом
                Пачаткове діюче знаечння струму трифазного КЗ: ${"%.2f".format(Ip0)} кА
               
               
            """.trimIndent()
        }
    }
}
