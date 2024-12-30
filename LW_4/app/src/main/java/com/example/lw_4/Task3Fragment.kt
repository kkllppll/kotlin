package com.example.lw_4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lw_4.R
import kotlin.math.sqrt
import kotlin.math.pow

class Task3Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task3, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Знаходимо елементи у макеті
        val inputUkmax = view.findViewById<EditText>(R.id.inputUkmax)
        val inputUvn = view.findViewById<EditText>(R.id.inputUvn)
        val inputSnom = view.findViewById<EditText>(R.id.inputSnom)
        val inputRcn = view.findViewById<EditText>(R.id.inputRcn)
        val inputXcn = view.findViewById<EditText>(R.id.inputXcn)
        val inputRcmin = view.findViewById<EditText>(R.id.inputRcmin)
        val inputXcmin = view.findViewById<EditText>(R.id.inputXcmin)
        val inputUnn = view.findViewById<EditText>(R.id.inputUnn)

        val inputLineLengths = view.findViewById<EditText>(R.id.inputLineLengths)
        val inputRline = view.findViewById<EditText>(R.id.inputRline)
        val inputXline = view.findViewById<EditText>(R.id.inputXline)

        val btnCalculate = view.findViewById<Button>(R.id.btnCalculate)
        val outputResults = view.findViewById<TextView>(R.id.outputResults)

        btnCalculate.setOnClickListener {
            // Отримуємо значення з полів
            val Ukmax = inputUkmax.text.toString().toDoubleOrNull() ?: 0.0
            val Uvn = inputUvn.text.toString().toDoubleOrNull() ?: 0.0
            val Snom = inputSnom.text.toString().toDoubleOrNull() ?: 0.0
            val Rcn = inputRcn.text.toString().toDoubleOrNull() ?: 0.0
            val Xcn = inputXcn.text.toString().toDoubleOrNull() ?: 0.0
            val Rcmin = inputRcmin.text.toString().toDoubleOrNull() ?: 0.0
            val Xcmin = inputXcmin.text.toString().toDoubleOrNull() ?: 0.0
            val Unn = inputUnn.text.toString().toDoubleOrNull() ?: 0.0


            val lengths = inputLineLengths.text.toString().split(",").map { it.trim().toDoubleOrNull() ?: 0.0 }
            val rLine = inputRline.text.toString().toDoubleOrNull() ?: 0.0
            val xLine = inputXline.text.toString().toDoubleOrNull() ?: 0.0

            // Реактивний опір трансформатора
            val Xt = (Ukmax * Uvn.pow(2)) / (100 * Snom)


            // Сумарний опір у нормальному режимі
            val XsumNorm = Xcn + Xt
            val ZshNorm = sqrt(Rcn.pow(2) + XsumNorm.pow(2))

            // Сумарний опір у мінімальному режимі
            val XsumMin = Xcmin + Xt
            val ZshMin = sqrt(Rcmin.pow(2) + XsumMin.pow(2))


            // Струми трифазного КЗ у нормальному режимі
            val Ish3Norm = (Uvn * 1000) / (sqrt(3.0) * ZshNorm)

            // Струми двофазного КЗ у нормальному режимі
            val Ish2Norm = Ish3Norm * (sqrt(3.0) / 2)


            // Струми трифазного КЗ у мінімальному режимі
            val Ish3Min = (Uvn * 1000) / (sqrt(3.0) * ZshMin)

            // Струми двофазного КЗ у мінімальному режимі
            val Ish2Min = Ish3Min * (sqrt(3.0) / 2)


            // Коефіцієнт приведення для визначення струмів на шинах
            val kpr = Unn.pow(2) / Uvn.pow(2)

            // Опори на шинах у нормальному режимі з коефіцієнтом приведення
            val RshNorm = Rcn * kpr
            val XshNorm = XsumNorm * kpr
            val ZshNormPr = sqrt(RshNorm.pow(2) + XshNorm.pow(2))


            // Опори на шинах у мінімальному режимі з коефіцієнтом приведення
            val RshMin = Rcmin * kpr
            val XshMin = XsumMin * kpr
            val ZshMinPr = sqrt(RshMin.pow(2) + XshMin.pow(2))

            // Дійсні струми трифазного КЗ на шинах 10 кВ у нормальному режимі
            val Ish3RealNorm = (Uvn * 1000) / (sqrt(3.0) * ZshNormPr)

            // Дійсні струми двофазного КЗ на шинах 10 кВ у нормальному режимі
            val Ish2RealNorm = Ish3RealNorm * (sqrt(3.0) / 2)

            //  Дійсні струми трифазного КЗ на шинах 10 кВ у мінімальному режимі
            val Ish3RealMin = (Uvn * 1000) / (sqrt(3.0) * ZshMinPr)

            // Дійсні струми двофазного КЗ на шинах 10 кВ у мінімальному режимі
            val Ish2RealMin = Ish3RealMin * (sqrt(3.0) / 2)



            // Розрахунок сумарних опорів ліній
            var RlineSum = 0.0
            var XlineSum = 0.0

            for (length in lengths) {
                RlineSum += rLine * length
                XlineSum += xLine * length
            }

            // Сумарний опір у нормальному режимі
            val RtotalNorm = RshNorm + RlineSum
            val XtotalNorm = XshNorm + XlineSum
            val ZtotalNorm = sqrt(RtotalNorm.pow(2) + XtotalNorm.pow(2))

            // Сумарний опір у мінімальному режимі
            val RtotalMin = RshMin + RlineSum
            val XtotalMin = XshMin + XlineSum
            val ZtotalMin = sqrt(RtotalMin.pow(2) + XtotalMin.pow(2))

            // Струми трифазного КЗ
            val I3phNorm = (Unn * 1000) / (sqrt(3.0) * ZtotalNorm)
            val I3phMin = (Unn * 1000) / (sqrt(3.0) * ZtotalMin)

            // Струми двофазного КЗ
            val I2phNorm = I3phNorm * (sqrt(3.0) / 2)
            val I2phMin = I3phMin * (sqrt(3.0) / 2)






            // Вивід результату
            outputResults.text = """
    Реактивний опір трансформатора Xt: ${"%.2f".format(Xt)} Ом
    Сумарний опір у нормальному режимі ZshNorm: ${"%.2f".format(ZshNorm)} Ом
    Сумарний опір у мінімальному режимі ZshMin: ${"%.2f".format(ZshMin)} Ом

    Струм трифазного КЗ у нормальному режимі Ish3Norm: ${"%.2f".format(Ish3Norm)} А
    Струм двофазного КЗ у нормальному режимі Ish2Norm: ${"%.2f".format(Ish2Norm)} А

    Струм трифазного КЗ у мінімальному режимі Ish3Min: ${"%.2f".format(Ish3Min)} А
    Струм двофазного КЗ у мінімальному режимі Ish2Min: ${"%.2f".format(Ish2Min)} А

    Коефіцієнт приведення kpr: ${"%.5f".format(kpr)}

    Опір на шинах у нормальному режимі RshNorm: ${"%.2f".format(RshNorm)} Ом
    Реактивний опір на шинах у нормальному режимі XshNorm: ${"%.2f".format(XshNorm)} Ом
    Сумарний опір на шинах у нормальному режимі ZshNormPr: ${"%.2f".format(ZshNormPr)} Ом

    Опір на шинах у мінімальному режимі RshMin: ${"%.2f".format(RshMin)} Ом
    Реактивний опір на шинах у мінімальному режимі XshMin: ${"%.2f".format(XshMin)} Ом
    Сумарний опір на шинах у мінімальному режимі ZshMinPr: ${"%.2f".format(ZshMinPr)} Ом

    Дійсний струм трифазного КЗ у нормальному режимі Ish3RealNorm: ${"%.2f".format(Ish3RealNorm)} А
    Дійсний струм двофазного КЗ у нормальному режимі Ish2RealNorm: ${"%.2f".format(Ish2RealNorm)} А

    Дійсний струм трифазного КЗ у мінімальному режимі Ish3RealMin: ${"%.2f".format(Ish3RealMin)} А
    Дійсний струм двофазного КЗ у мінімальному режимі Ish2RealMin: ${"%.2f".format(Ish2RealMin)} А
    
    Сумарний активний опір лінії: ${"%.2f".format(RlineSum)} Ом
    Сумарний реактивний опір лінії: ${"%.2f".format(XlineSum)} Ом
        
        Опір у нормальному режимі:
        RtotalNorm: ${"%.2f".format(RtotalNorm)} Ом
        XtotalNorm: ${"%.2f".format(XtotalNorm)} Ом
        ZtotalNorm: ${"%.2f".format(ZtotalNorm)} Ом
        
        Опір у мінімальному режимі:
        RtotalMin: ${"%.2f".format(RtotalMin)} Ом
        XtotalMin: ${"%.2f".format(XtotalMin)} Ом
        ZtotalMin: ${"%.2f".format(ZtotalMin)} Ом

        Струми КЗ у нормальному режимі:
        Iш3 (трифазний): ${"%.2f".format(I3phNorm)} А
        Iш2 (двофазний): ${"%.2f".format(I2phNorm)} А

        Струми КЗ у мінімальному режимі:
        Iш3 (трифазний): ${"%.2f".format(I3phMin)} А
        Iш2 (двофазний): ${"%.2f".format(I2phMin)} А
""".trimIndent()

        }
    }
}
