package com.example.ipcaboleias.rides

data class Ride(
    val uid : String = "",
    var name : String = "",
    var car : String = "",
    var profilePicture : String = "",
    val date: String = "",
    val time: String = "",
    val startLatitude : Double = 0.0,
    val startLongitude : Double = 0.0,
    val endLatitude : Double = 0.0,
    val endLongitude : Double = 0.0,
    val type : String = "",
    val places : Int = 0,
    val description : String = "",
    val acceptAlunos : Boolean = false,
    val acceptDoc : Boolean = false,
    val uniqueRide : Boolean = true,
    val price : Double? = 0.0
)

