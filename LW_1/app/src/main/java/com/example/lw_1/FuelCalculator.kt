package com.example.lw_1

class FuelCalculator {
    var hydrogenP: Double = 0.0
    var carbonP: Double = 0.0
    var sulfurP: Double = 0.0
    var nitrogenP: Double = 0.0
    var oxygenP: Double = 0.0
    var waterP: Double = 0.0
    var ashP: Double = 0.0

    fun inputFuelComponents(hydrogen: Double, carbon: Double, sulfur: Double, nitrogen: Double, oxygen: Double, water: Double, ash: Double) {
        this.hydrogenP = hydrogen
        this.carbonP = carbon
        this.sulfurP = sulfur
        this.nitrogenP = nitrogen
        this.oxygenP = oxygen
        this.waterP = water
        this.ashP = ash
    }

    fun coeffWorkingToDry(): Pair<Map<String, Double>, Double> {
        val coeffWorkingToDry = 100 / (100 - waterP)

        val composition = mapOf(
            "Hydrogen" to hydrogenP * coeffWorkingToDry,
            "Carbon" to carbonP * coeffWorkingToDry,
            "Sulfur" to sulfurP * coeffWorkingToDry,
            "Nitrogen" to nitrogenP * coeffWorkingToDry,
            "Oxygen" to oxygenP * coeffWorkingToDry,
            "Ash" to ashP * coeffWorkingToDry
        )

        val totalPercentage = composition.values.sum()

        if (totalPercentage < 99.0 || totalPercentage > 101.0) {
            throw IllegalArgumentException("Сума компонентів сухої маси повинна бути близько 100%, але отримано $totalPercentage%.")
        }

        return Pair(composition, coeffWorkingToDry)
    }

    fun coeffWorkingToComb(): Pair<Map<String, Double>, Double> {
        val coeffWorkingToComb = 100 / (100 - waterP - ashP)

        val composition = mapOf(
            "Hydrogen" to hydrogenP * coeffWorkingToComb,
            "Carbon" to carbonP * coeffWorkingToComb,
            "Sulfur" to sulfurP * coeffWorkingToComb,
            "Nitrogen" to nitrogenP * coeffWorkingToComb,
            "Oxygen" to oxygenP * coeffWorkingToComb
        )

        val totalPercentage = composition.values.sum()

        if (totalPercentage < 99.0 || totalPercentage > 101.0) {
            throw IllegalArgumentException("Сума компонентів горючої маси повинна бути близько 100%, але отримано $totalPercentage%.")
        }

        return Pair(composition, coeffWorkingToComb)
    }

    fun calculateLowerHeatingValueWorkingMass(): Double {
        return 339 * carbonP + 1030 * hydrogenP - 108.8 * (oxygenP - sulfurP) - 25 * waterP
    }

    fun calculateLowerHeatingValueDryMass(working_Q: Double): Double {
        return (working_Q + 0.025 * waterP) * 100 / (100 - waterP)
    }

    fun calculateLowerHeatingValueCombustibleMass(working_Q: Double): Double {
        return (working_Q + 0.025 * waterP) * 100 / (100 - waterP - ashP)
    }
}
