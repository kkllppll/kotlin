package com.example.lw_2

class FuelCalculator {
    var Q_iR: Double = 0.0
    var L: Double = 0.0
    var Ar: Double = 0.0
    var G: Double = 0.0
    var n: Double = 0.0
    var k: Double = 0.0
    var B: Double = 0.0

    fun inputParameters(
        Q_iR: Double,
        L: Double,
        Ar: Double,
        G: Double,
        n: Double,
        k: Double,
        B: Double,

    ) {
        this.Q_iR = Q_iR
        this.L = L
        this.Ar = Ar
        this.G = G
        this.n = n
        this.k = k
        this.B = B
    }

    fun calculateEmissionFactor(): Double {
        if (Q_iR == 0.0) throw IllegalArgumentException("Нижча теплота згоряння не може бути нульовою.")
        return (1_000_000 / Q_iR) * L * (Ar / (100 - G )) * ( 1 - n) + k
    }

    fun calculateTotalEmission(): Double {
        val emissionFactor = calculateEmissionFactor()
        return 0.000001 * emissionFactor  * Q_iR * B
    }
}
