package com.example.lw_3

import kotlin.math.exp
import kotlin.math.sqrt
import kotlin.math.PI
import kotlin.math.pow

class Calculator {
    var Pc: Double = 0.0
    var sigma_P: Double = 0.0
    var sigma_L_P: Double = 0.0
    var B: Double = 0.0


    fun inputParameters(Pc: Double, sigma_P: Double, sigma_L_P: Double, B: Double) {
        this.Pc = Pc
        this.sigma_P = sigma_P
        this.sigma_L_P = sigma_L_P
        this.B = B
    }

    // функція нормального розподілу для потужності P_d
    private fun P_d(P: Double, sigma: Double): Double {
        return (1 / (sigma * sqrt(2 * PI))) * exp(-((P - Pc).pow(2)) / (2 * sigma.pow(2)))
    }

    // чисельне інтегрування для розрахунку delta_W
    private fun calculateDeltaW(P_min: Double, P_max: Double, delta_P: Double, sigma: Double): Double {
        var sum = 0.0
        var P = P_min
        // інтегрування методом прямокутників
        while (P <= P_max) {
            sum += P_d(P, sigma) * delta_P
            P += delta_P
        }
        return sum
    }

    // розрахунок згенерованої енергії W
    private fun calculateEnergy(delta_W: Double): Double {
        val hoursPerDay = 24
        return delta_W * Pc * hoursPerDay
    }

    // розрахунок енергії, яка генерується з небалансами
    private fun calculatePenaltyEnergy(delta_W: Double): Double {
        val hoursPerDay = 24
        return Pc * hoursPerDay * (1 - delta_W)
    }

    // розрахунок прибутку
    private fun calculateProfit(W: Double): Double {
        return W * B
    }

    // розрахунок штрафів
    private fun calculatePenalty(W_total: Double, W_safe: Double): Double {
        // Штраф = небалансна частка енергії * вартість енергії
        val W_penalty = W_total - W_safe
        return W_penalty * B
    }

    // головна функція для розрахунку всіх значень
    fun calculate(): Map<String, Double> {
        val delta_P = 0.001 // малий крок інтегрування для високої точності
        val P_min = 4.75 //нижня межа інтегрування
        val P_max = 5.25 // верхня межа інтегрування

        // розрахунок delta_W1 (до вдосконалення)
        val delta_W1 = calculateDeltaW(P_min, P_max, delta_P, sigma_P)

        // розрахунок delta_W2 (після вдосконалення)
        val delta_W2 = calculateDeltaW(P_min, P_max, delta_P, sigma_L_P)

        // розрахунок згенерованої енергії
        val W1 = calculateEnergy(delta_W1) // До вдосконалення
        val W2 = calculateEnergy(delta_W2) // Після вдосконалення

        // розрахунок енергії з небалансами
        val W_penalty1 = calculatePenaltyEnergy(delta_W1)
        val W_penalty2 = calculatePenaltyEnergy(delta_W2) //

        // розрахунок прибутку
        val profit1 = calculateProfit(W1) // до вдосконалення
        val profit2 = calculateProfit(W2) // після вдосконалення

        // розрахунок штрафів
        val W_total = calculateEnergy(1.0) // загальна енергія (без втрат)
        val penalty1 = calculatePenalty(W_total, W1) // штраф до вдосконалення
        val penalty2 = calculatePenalty(W_total, W2) // штраф після вдосконалення

        val netProfit1 = profit1 - penalty1 // чистий прибуток до вдосконалення
        val netProfit2 = profit2 - penalty2 // чистий прибуток після вдосконалення

        // повернення всіх результатів
        return mapOf(
            "delta_W1" to delta_W1 * 100, // перетворення в проценти
            "W1" to W1, // згенерована енергія до вдосконалення
            "profit1" to profit1, // прибуток до вдосконалення
            "penalty1" to penalty1, // штраф до вдосконалення
            "netProfit1" to netProfit1, // чистий прибуток до вдосконалення
            "W_penalty1" to W_penalty1, // енергія з небалансами
            "delta_W2" to delta_W2 * 100, // перетворення в проценти
            "W2" to W2, // згенерована енергія після вдосконалення
            "profit2" to profit2, // прибуток після вдосконалення
            "penalty2" to penalty2, // штраф після вдосконалення
            "netProfit2" to netProfit2, // чистий прибуток після вдосконалення
            "W_penalty2" to W_penalty2 // енергія з небалансами
        )
    }
}
