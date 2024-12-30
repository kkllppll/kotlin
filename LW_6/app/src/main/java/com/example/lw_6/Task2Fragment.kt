package com.example.lw_6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlin.math.sqrt

class Task2Fragment : Fragment() {

    private lateinit var container: LinearLayout // контейнер для динамічних груп
    private lateinit var resultText: TextView // поле для виводу результатів

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task, container, false) // використовуємо спільний макет
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ідентифікуємо елементи
        container = view.findViewById(R.id.mainContainer)
        val btnAddGroup = view.findViewById<Button>(R.id.btnAddGroup)
        val btnCalculate = view.findViewById<Button>(R.id.btnCalculate)
        resultText = view.findViewById(R.id.outputResults)

        // додаємо групу при натисканні кнопки
        btnAddGroup.setOnClickListener {
            val groupView = layoutInflater.inflate(R.layout.input_group, container, false)

            // знаходимо кнопку видалення в доданій групі
            val btnRemoveGroup = groupView.findViewById<Button>(R.id.btnRemoveGroup)
            btnRemoveGroup.setOnClickListener {
                container.removeView(groupView) // видаляємо групу з контейнера
            }

            container.addView(groupView)
        }

        // розрахунок результатів
        btnCalculate.setOnClickListener {
            calculateResults()
        }
    }

    private fun calculateResults() {
        var numerator = 0.0 // Сума n * P * k
        var denominator = 0.0 // Сума n * P
        var powerSum = 0.0 // Сума n * P
        var powerSquaredSum = 0.0 // Сума n * P^2
        var reactivePowerSum = 0.0 // Сума n * P * k * tgφ
        var totalVoltage = 0.0 // Загальна напруга
        var voltageCount = 0 // Лічильник напруг

        for (i in 0 until container.childCount) {
            val groupView = container.getChildAt(i)

            val quantity =
                groupView.findViewById<EditText>(R.id.et_quantity)?.text?.toString()?.toIntOrNull()
                    ?: 0
            val power =
                groupView.findViewById<EditText>(R.id.et_power)?.text?.toString()?.toDoubleOrNull()
                    ?: 0.0
            val usageCoeff = groupView.findViewById<EditText>(R.id.et_usage_coeff)?.text?.toString()
                ?.toDoubleOrNull() ?: 0.0
            val tgPhi =
                groupView.findViewById<EditText>(R.id.et_tg_phi)?.text?.toString()?.toDoubleOrNull()
                    ?: 0.0
            val voltage = groupView.findViewById<EditText>(R.id.et_voltage)?.text?.toString()
                ?.toDoubleOrNull()

            if (voltage != null && voltage > 0) {
                totalVoltage += voltage
                voltageCount++
            }

            numerator += quantity * power * usageCoeff
            denominator += quantity * power
            powerSum += quantity * power
            powerSquaredSum += quantity * power * power
            reactivePowerSum += quantity * power * usageCoeff * tgPhi
        }

        // розрахунок середньої напруги
        val Uh = if (voltageCount > 0) totalVoltage / voltageCount else 0.0

        if (Uh == 0.0) {
            resultText.text = "Напруга не задана або рівна нулю для всіх груп."
            return
        }

        // груповий коефіцієнт використання:
        val Kv = if (denominator > 0) numerator / denominator else 0.0

        //  ефективна кількість ЕП:
        val effectiveEP = if (powerSquaredSum > 0) (powerSum * powerSum) / powerSquaredSum else 0.0

        // розрахунковий коефіцієнт активної потужності
        val Kp = getActivePowerCoefficient(Kv, effectiveEP)

        //  розрахункове активне навантаження:
        val Pp = Kp * numerator

        //  розрахункове реактивне навантаження:
        val Qp = reactivePowerSum

        //  повна потужність:
        val Sp = Math.sqrt(Pp * Pp + Qp * Qp)

        // озрахунковий груповий струм
        val Ip = Pp / Uh

        val result = """
        n * Ph = %.2f кВТ
        N * Ph * Kv: %.2f
        Розрахункове реактивне навантаження (Qp): %.2f кВАр
        Ефективна кількість Еп: %.2f
        Розрахунковий груповий струм (Ip): %.2f А
    """.trimIndent().format(denominator, numerator, Qp, powerSquaredSum, Ip)

        resultText.text = result
    }

}

    private fun findClosestInTable(value: Double, values: List<Double>): Int {
        // якщо точне значення є в списку, повертаємо його індекс
        if (value in values) return values.indexOf(value)

        // якщо значення менше найменшого або більше найбільшого, повертаємо крайній індекс
        if (value < values.first()) return 0
        if (value > values.last()) return values.lastIndex

        // знаходимо два сусідні значення, між якими знаходиться value
        for (i in 0 until values.size - 1) {
            if (value > values[i] && value < values[i + 1]) {
                // повертаємо найближче за різницею
                return if (value - values[i] < values[i + 1] - value) i else i + 1
            }
        }
        return 0 // якщо щось пішло не так
    }


    private fun getActivePowerCoefficient(Kv: Double, ne: Double): Double {
        val kvValues = listOf(0.1, 0.15, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8)
        val neValues = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16, 18, 20, 25, 30, 35, 40, 50, 60, 80, 100)
            .map { it.toDouble() } // конвертація Int у Double

        val kpTable = listOf(
            listOf(8.00, 5.33, 4.00, 2.67, 2.00, 1.60, 1.33, 1.14, 1.00),
            listOf(6.22, 4.33, 3.39, 2.45, 1.98, 1.60, 1.33, 1.14, 1.00),
            listOf(4.06, 2.89, 2.31, 1.74, 1.45, 1.34, 1.22, 1.14, 1.00),
            listOf(3.24, 2.35, 1.91, 1.47, 1.25, 1.21, 1.12, 1.06, 1.00),
            listOf(2.84, 2.09, 1.72, 1.35, 1.16, 1.16, 1.08, 1.03, 1.00),
            listOf(2.64, 1.96, 1.62, 1.28, 1.14, 1.13, 1.06, 1.01, 1.00),
            listOf(2.49, 1.86, 1.54, 1.23, 1.12, 1.10, 1.04, 1.01, 1.00),
            listOf(2.37, 1.78, 1.48, 1.19, 1.10, 1.08, 1.02, 1.01, 1.00),
            listOf(2.27, 1.71, 1.43, 1.16, 1.09, 1.07, 1.01, 1.01, 1.00),
            listOf(2.18, 1.65, 1.39, 1.13, 1.07, 1.05, 1.01, 1.01, 1.00),
            listOf(2.04, 1.56, 1.32, 1.08, 1.05, 1.03, 1.00, 1.00, 1.00),
            listOf(1.94, 1.49, 1.27, 1.05, 1.02, 1.02, 1.00, 1.00, 1.00),
            listOf(1.85, 1.43, 1.23, 1.02, 1.00, 1.00, 1.00, 1.00, 1.00),
            listOf(1.78, 1.39, 1.19, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00),
            listOf(1.72, 1.35, 1.16, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00),
            listOf(1.60, 1.27, 1.10, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00),
            listOf(1.51, 1.21, 1.05, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00),
            listOf(1.44, 1.16, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00),
            listOf(1.30, 1.07, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00),
            listOf(1.25, 1.03, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00),
            listOf(1.16, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00),
            listOf(1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00)
        )

        val closestKvIndex = findClosestInTable(Kv, kvValues)
        val closestNeIndex = findClosestInTable(ne, neValues)

        return kpTable[closestNeIndex][closestKvIndex]
    }

