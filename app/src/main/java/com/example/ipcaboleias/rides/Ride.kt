package com.example.ipcaboleias.rides

import com.example.ipcaboleias.createPublication.NewStop
import com.google.firebase.Timestamp
import java.sql.ClientInfoStatus
import java.sql.Date
import java.sql.Time
import java.time.DateTimeException

class Ride() {
    var uid: String = ""
    var startLatitute: Double = 0.0
    var startLongitude: Double = 0.0
    var endLatitute: Double = 0.0
    var endLongitude: Double = 0.0
    var dateTime: Timestamp = Timestamp.now()
    var type: String = ""
    var places: Int = 1
    var description: String = ""
    var acceptDoc: Boolean = false
    var acceptAlunos: Boolean = false
    var price: Float = 0.0f
    var status: Boolean = false
    var stops: MutableList<NewStop> = ArrayList()
}

