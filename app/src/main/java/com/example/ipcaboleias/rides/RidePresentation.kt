package com.example.ipcaboleias.rides

import com.example.ipcaboleias.createPublication.NewStop
import com.google.firebase.Timestamp
import java.sql.Date
import java.sql.Time

data class RidePresentation (
    val uid : String = "",
    var name : String = "",
    var email : String = "",
    var car : String = "",
    var carColor : String = "",
    var profilePicture : String = "",
    var date: Timestamp = Timestamp.now(),
    val startLatitude : Double = 0.0,
    val startLongitude : Double = 0.0,
    val endLatitude : Double = 0.0,
    val endLongitude : Double = 0.0,
    val type : String = "",
    var places : Int = 0,
    var description : String = "",
    val acceptAlunos : Boolean = false,
    val acceptDoc : Boolean = false,
    val uniqueRide : Boolean = true,
    var price : Float? = 0.0f,
    var stops: MutableList<NewStop> = ArrayList()
)