package com.example.finalproject.weather

class Model {
    data class Result(
        val coord: Coord, // City geo location
        val weather: List<Weather>, // Weather conditions
        val base: String, // Internal parameter
        val main: Main, // Atmospheric conditions
        val visibility: Int, // Visibility, meter. The maximum value of the visibility is 10km
        val wind: Wind, // Wind information
        val rain: Rain, // Rain volume
        val clouds: Clouds, // Cloudiness
        val deltaTime: Long, // Time of data calculation, unix, UTC
        val sys: Sys, // Location information
        val timezone: Long, // Shift in seconds from UTC
        val cityID: Int, // City ID
        val cityName: String, // City name
        val cod: Int // Internal parameter
    )

    data class Coord(
        val lat: Float, // City geo location, latitude
        val lon: Float // City geo location, longitude
    )

    data class Weather(
        val id: Int, // Weather condition id
        val main: String, // Group of weather parameters (Rain, Snow, Extreme etc.)
        val description: String, // Weather condition within the group
        val icon: String // Weather icon id
    )

    data class Main(
        val temp: Float, // Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
        val feelsLike: Float, // Temperature. This temperature parameter accounts for the human perception of weather. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
        val tempMin: Float, // Minimum temperature at the moment. This is minimal currently observed temperature (within large megalopolises and urban areas). Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
        val tempMax: Float, // Maximum temperature at the moment. This is maximal currently observed temperature (within large megalopolises and urban areas). Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
        val pressure: Float, // Atmospheric pressure (on the sea level, if there is no sea level or ground level data), hPa
        val humidity: Float, // Humidity, %
        val seaLevel: Float, // Atmospheric pressure on the sea level, hPa
        val groundLevel: Float // Atmospheric pressure on the ground level, hPa
    )

    data class Wind(
        val speed: Float, // Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
        val deg: Int, // Wind direction, degrees (meteorological)
        val gust: Float // Wind gust. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour
    )

    data class Rain(
        val oneHour: Float // Rain volume for the last 1 hour, mm
    )

    data class Clouds(
        val all: Float // Cloudiness, %
    )

    data class Sys(
        val type: Int, // Internal parameter
        val id: Int, // Internal parameter
        val country: String, // Country code (GB, JP etc.)
        val sunrise: Long, // Sunrise time, unix, UTC
        val sunset: Long // Sunset time, unix, UTC
    )
}