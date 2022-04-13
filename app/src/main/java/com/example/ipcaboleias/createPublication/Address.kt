package com.example.ipcaboleias.createPublication

data class ReverseGeo(
    val place_id: Int,
    val licence: String,
    val osm_type: String,
    val osm_id: Int,
    val lat: Double,
    val lon: Double,
    val display_name: String,
    val address: Address,
    val boundingbox: List<Double>
)

data class Address(
    val neighbourhood: String,
    val city_district: String,
    val town: String,
    val county: String,
    val postcode: String,
    val country: String,
    val country_code: String
)
