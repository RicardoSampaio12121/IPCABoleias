package com.example.ipcaboleias.history

data class PassangerPresentation(
    val uid: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val name: String,
    val surname : String,
    val email : String,
    val carBrand : String?,
    val carModel : String?,
    val carColor : String?,
    val carPlate : String?,
    val profilePicture : String

)