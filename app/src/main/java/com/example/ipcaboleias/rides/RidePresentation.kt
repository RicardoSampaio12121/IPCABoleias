package com.example.ipcaboleias.rides

data class RidePresentation (
    val uid : String = "",
    var name : String = "",
    var email : String = "",
    var car : String = "",
    var carColor : String = "",
    var profilePicture : String = "",
    val date: String = "",
    val time: String = "",
    val startLatitude : Double = 0.0,
    val startLongitude : Double = 0.0,
    val endLatitude : Double = 0.0,
    val endLongitude : Double = 0.0,
    val type : String = "",
    var places : Int = 0,
    val description : String = "",
    val acceptAlunos : Boolean = false,
    val acceptDoc : Boolean = false,
    val uniqueRide : Boolean = true,
    var price : Float? = 0.0f
)