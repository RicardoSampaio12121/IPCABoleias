package com.example.ipcaboleias.history

import com.google.firebase.Timestamp
import java.sql.Date
import java.sql.Time

data class ScheduledRidePresentation(
    var driverId: String = "",
    var name: String = "",
    var doc: Boolean = false,
    var startLat: Double = 0.0,
    var startLong: Double = 0.0,
    var userStartLat: Double = 0.0,
    val userStartLong: Double = 0.0,
    var endLat: Double = 0.0,
    var endLong: Double = 0.0,
    var date: Timestamp = Timestamp.now(),
    var profilePicture: String = ""
)
