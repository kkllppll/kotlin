package com.example.lw_6

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.math.sqrt

class Task3Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // прив'язуємо макет
        return inflater.inflate(R.layout.fragment_task3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val btnCalculate = view.findViewById<Button>(R.id.btnCalculate)
        val resultText = view.findViewById<TextView>(R.id.outputResults)


        btnCalculate.setOnClickListener {
            val quantity =
                view.findViewById<EditText>(R.id.et_quantity)?.text?.toString()?.toIntOrNull()
                    ?: 0.0
            val Phi =
                view.findViewById<EditText>(R.id.et_Phi)?.text?.toString()?.toDoubleOrNull() ?: 0.0
            val nPhKv =
                view.findViewById<EditText>(R.id.et_nPhKv)?.text?.toString()?.toDoubleOrNull()
                    ?: 0.0
            val nPnKvtg =
                view.findViewById<EditText>(R.id.et_nPnKvtg)?.text?.toString()?.toDoubleOrNull()
                    ?: 0.0
            val np2 =
                view.findViewById<EditText>(R.id.et_np2)?.text?.toString()?.toDoubleOrNull() ?: 0.0



            val numeratorKv = nPhKv
            val denominatorKv = Phi

            // коефіцієнти використання цеху в цілому:
            val Kv = if (denominatorKv > 0) numeratorKv / denominatorKv else 0.0


            val numeratorNe = Phi * Phi
            val denominatorNe = np2

            // ефективна кількість ЕП цеху в цілому:
            val Ne = if (denominatorNe > 0) numeratorNe / denominatorNe else 0.0

            // розрахунковий коефіцієнт активної потужності по таблиці 6.4
            val Kp = getSimultaneityCoefficient(Kv, quantity.toInt())

            // розрахункове активне навантаження
            val Pp = Kp * nPhKv

            //  розрахункове реактивне навантаження
            val Qp = Kp * nPnKvtg

            // повна потужність
            val Sp = Math.sqrt(Pp * Pp + Qp * Qp)

            val Uh = 0.38

            // розрахунковий груповий струм
            val Ip = Pp / Uh

            // Ррзультати
            val result = """
                    Коефіцієнт використання цеху (Kv): %.4f
                    Ефективна кількість ЕП (Ne): %.2f
                    Розрахунковий коефіцієнт активної потужності (Kp): %.2f
                    Активне навантаження (Pp): %.2f кВт
                    Реактивне навантаження (Qp): %.2f кВАр
                    Повна потужність (Sp): %.2f кВА
                    Розрахунковий струм (Ip): %.2f А
                """.trimIndent().format(Kv, Ne, Kp, Pp, Qp, Sp, Ip)

            resultText.text = result
        }
    }
}






private fun getSimultaneityCoefficient(Kv: Double, connections: Int): Double {
    // значення діапазонів Kv
    val kvRanges = listOf(
        0.3 to 0.5, // 0.3 ≤ Kv < 0.5
        0.5 to 0.8, // 0.5 ≤ Kv < 0.8
        0.8 to Double.MAX_VALUE // Kv ≥ 0.8
    )

    // таблиця значень Ko для різної кількості приєднань
    val koTable = listOf(
        listOf(0.9, 0.8, 0.75, 0.7), // Kv < 0.3
        listOf(0.95, 0.9, 0.85, 0.8), // 0.3 ≤ Kv < 0.5
        listOf(1.0, 0.95, 0.9, 0.85), // 0.5 ≤ Kv < 0.8
        listOf(1.0, 1.0, 0.95, 0.9)  // Kv ≥ 0.8
    )

    val kvIndex = when {
        Kv < kvRanges[0].first -> 0 // Kv < 0.3
        Kv >= kvRanges[0].first && Kv < kvRanges[0].second -> 1 // 0.3 ≤ Kv < 0.5
        Kv >= kvRanges[1].first && Kv < kvRanges[1].second -> 2 // 0.5 ≤ Kv < 0.8
        else -> 3 // Kv ≥ 0.8
    }


    // визначаємо індекс для кількості приєднань
    val connectionsIndex = when (connections) {
        in 2..4 -> 0
        in 5..8 -> 1
        in 9..25 -> 2
        else -> 3 // >25
    }

    // повертаємо відповідне значення Ko з таблиці
    return koTable[kvIndex][connectionsIndex]
}





