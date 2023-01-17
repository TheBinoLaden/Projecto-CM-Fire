package com.example.finalproject.weather

import java.text.Normalizer
import java.util.*

class District {
    data class District(
            val name: String, // District name
            val lat: Float, // District geo location, latitude
            val lon: Float // District geo location, longitude
    )

    companion object {
        val Aveiro = District("aveiro", 1f, 1f)
        val Beja = District("beja", 1f, 1f)
        val Braga = District("braga", 1f, 1f)
        val Braganca = District("braganca", 1f, 1f)
        val CasteloBranco = District("castelo", 1f, 1f)
        val Coimbra = District("coimbra", 1f, 1f)
        val Evora = District("evora", 1f, 1f)
        val Faro = District("faro", 1f, 1f)
        val Guarda = District("guarda", 1f, 1f)
        val Leiria = District("leiria", 1f, 1f)
        val Lisboa = District("lisboa", 1f, 1f)
        val Portalegre = District("portalegre", 1f, 1f)
        val Porto = District("porto", 1f, 1f)
        val Santarem = District("santarem", 1f, 1f)
        val Setubal = District("setubal", 1f, 1f)
        val VianaDoCastelo = District("viana", 1f, 1f)
        val VilaReal = District("vila", 1f, 1f)
        val Viseu = District("viseu", 1f, 1f)
        val Districts = listOf(
                Aveiro, Beja, Braga, Braganca, CasteloBranco, Coimbra, Evora, Faro, Guarda, Leiria, Lisboa, Portalegre,
                Porto, Santarem, Setubal, VianaDoCastelo, VilaReal, Viseu
        )

        fun getDistrict(districtName: String): District? {
            val normalized = Normalizer.normalize(districtName, Normalizer.Form.NFD)
            val districtNorm = normalized.replace("[^\\p{ASCII}]".toRegex(), "")
                    .lowercase(Locale.ROOT).substringBefore(" ")
            for (district in Districts)
                if (district.name == districtNorm) return district
            return null
        }
    }
}