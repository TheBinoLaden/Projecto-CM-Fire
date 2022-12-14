package com.example.finalproject.weather

class Formulas {
    companion object {
        fun rainfallFactor(rainfallVolume: Float): Float {
            return if (rainfallVolume > 0F && rainfallVolume <= 1F) 1F
            else if (rainfallVolume > 1F && rainfallVolume <= 2F) 0.8F
            else if (rainfallVolume > 2F && rainfallVolume <= 3F) 0.6F
            else if (rainfallVolume > 3F && rainfallVolume <= 4F) 0.4F
            else if (rainfallVolume > 4F && rainfallVolume <= 10F) 0.2F
            else if (rainfallVolume > 10F) 0.1F
            else 0F
        }

        fun ignitionIndexFactor(ignitionIndex: Float): Int {
            return if (ignitionIndex < 151F) 1
            else if (ignitionIndex >= 151F && ignitionIndex < 301F) 2
            else if (ignitionIndex >= 301F && ignitionIndex < 451F) 3
            else if (ignitionIndex >= 451F && ignitionIndex < 601F) 4
            else if (ignitionIndex >= 601F && ignitionIndex < 751F) 5
            else 6
        }

        fun cumulativeIndexFactor(cumulativeIndex: Float): Int {
            return if (cumulativeIndex < 301F) 1
            else if (cumulativeIndex >= 301F && cumulativeIndex < 1001F) 2
            else if (cumulativeIndex >= 1001F && cumulativeIndex < 2001F) 3
            else if (cumulativeIndex >= 2001F && cumulativeIndex < 4001F) 4
            else if (cumulativeIndex >= 4001F && cumulativeIndex < 6001F) 5
            else if (cumulativeIndex >= 6001F && cumulativeIndex < 8001F) 6
            else if (cumulativeIndex >= 8001F && cumulativeIndex < 10001F) 7
            else if (cumulativeIndex >= 10001F && cumulativeIndex < 12001F) 8
            else if (cumulativeIndex >= 12001F && cumulativeIndex < 15001F) 9
            else if (cumulativeIndex >= 15001F && cumulativeIndex < 18001F) 10
            else if (cumulativeIndex >= 18001F && cumulativeIndex < 21001F) 11
            else if (cumulativeIndex >= 21001F && cumulativeIndex < 24001F) 12
            else if (cumulativeIndex >= 24001F && cumulativeIndex < 27001F) 13
            else if (cumulativeIndex >= 27001F && cumulativeIndex < 31001F) 14
            else if (cumulativeIndex >= 31001F && cumulativeIndex < 35001F) 15
            else if (cumulativeIndex >= 35001F && cumulativeIndex < 39001F) 16
            else if (cumulativeIndex >= 39001F && cumulativeIndex < 43001F) 17
            else if (cumulativeIndex >= 43001F && cumulativeIndex < 47001F) 18
            else if (cumulativeIndex >= 47001F && cumulativeIndex < 51001F) 19
            else 20
        }

        fun windFactor(windSpeed: Float): Int {
            return if (windSpeed <= 10F) 0
            else if (windSpeed > 10F && windSpeed <= 15F) 1
            else if (windSpeed > 15F && windSpeed <= 20F) 2
            else if (windSpeed > 20F && windSpeed <= 30F) 3
            else if (windSpeed > 30F && windSpeed <= 40F) 4
            else 5
        }

        fun dewPoint(airTemperature: Float, relativeHumidity: Float): Float {
            return airTemperature - ((100 - relativeHumidity) / 5)
        }

        fun ignitionIndex(airTemperature: Float, relativeHumidity: Float): Float {
            return airTemperature * (airTemperature - dewPoint(airTemperature, relativeHumidity))
        }

        // maybe use this https://openweathermap.org/forecast5
        fun cumulativeIndex(airTemperature: Float, relativeHumidity: Float, rainfallVolume: Float): Float {
            var sum = 0F
            //TODO for loop incorrect, replace by last day temps
            sum += ignitionIndex(airTemperature, relativeHumidity)
            return rainfallFactor(rainfallVolume) * sum
        }

        fun fireDangerIndex(airTemperature: Float, relativeHumidity: Float, rainfallVolume: Float, windSpeed: Float): Int {
            return ignitionIndexFactor(ignitionIndex(airTemperature, relativeHumidity)) +
                    cumulativeIndexFactor(cumulativeIndex(airTemperature, relativeHumidity, rainfallVolume)) +
                    windFactor(windSpeed)
        }

        fun fdiInterpretation(fireDangerIndex: Int): String {
            return when (fireDangerIndex) {
                in 0..1 -> "None"
                in 2..4 -> "Low"
                in 5..8 -> "Moderate"
                in 9..12 -> "High"
                in 13..17 -> "Severe"
                else -> "Extreme"
            }
        }
    }
}