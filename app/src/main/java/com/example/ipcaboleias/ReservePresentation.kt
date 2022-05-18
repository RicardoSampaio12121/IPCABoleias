package com.example.ipcaboleias

import com.google.firebase.Timestamp

data class ReservePresentation(
    var docId: String = "",
    var passengerId: String = "",
    var name: String = "",
    var doc: Boolean = false,
    var startLat: Double = 0.0,
    var startLong: Double = 0.0,
    var endLat: Double = 0.0,
    var endLong: Double = 0.0,
    var date: Timestamp = Timestamp.now(),
    var profilePicture: String = ""
)
