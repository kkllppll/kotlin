package com.example.lw_1

class MazutCalculator {
    var carbonP: Double = 0.0
    var hydrogenP: Double = 0.0
    var oxygenP: Double = 0.0
    var sulfurP: Double = 0.0
    var lowerHeating_Q: Double = 0.0
    var waterP: Double = 0.0
    var ashP: Double = 0.0
    var vanadiumP: Double = 0.0

    fun inputFuelComponents(
        carbon: Double,
        hydrogen: Double,
        oxygen: Double,
        sulfur: Double,
        lowerHeating_Q: Double,
        water: Double,
        ash: Double,
        vanadium: Double
    ) {

        this.carbonP = carbon
        this.hydrogenP = hydrogen
        this.oxygenP = oxygen
        this.sulfurP = oxygen
        this.lowerHeating_Q = lowerHeating_Q
        this.waterP = water
        this.ashP = ash
        this.vanadiumP = vanadium
    }

    fun CombToWorking(): Pair<Map<String, Double>, Double> {
        val coeffCombToWorking = (100 - waterP - ashP) / 100
        val coeffCombToWorking_2 = (100 - waterP) / 100

        val composition = mapOf(


            "Carbon" to carbonP * coeffCombToWorking,
            "Hydrogen" to hydrogenP * coeffCombToWorking,
            "Oxygen" to oxygenP * coeffCombToWorking,
            "Sulfur" to sulfurP * coeffCombToWorking,
            "Ash" to ashP * coeffCombToWorking_2,
            "Vandium" to vanadiumP * coeffCombToWorking_2
        )


        return Pair(composition, coeffCombToWorking)
    }


    fun calculateWorkingHeatingValue(lowerHeating_Q: Double): Double {
        return lowerHeating_Q * (100 - waterP - ashP) / 100 - 0.025 * waterP
    }

}

