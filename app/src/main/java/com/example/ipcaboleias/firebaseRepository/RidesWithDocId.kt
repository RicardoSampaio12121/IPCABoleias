package com.example.ipcaboleias.firebaseRepository

import com.google.firebase.Timestamp

data class RidesWithDocId(
    var docId: String = "",
    var uid: String = "",
    var startLatitute: Double = 0.0,
    var startLongitude: Double = 0.0,
    var endLatitute: Double = 0.0,
    var endLongitude: Double = 0.0,
    var dateTime: Timestamp = Timestamp.now(),
    var type: String = "",
    var places: Int = 1,
    var description: String = "",
    var uniqueDrive: Boolean = false,
    var acceptDoc: Boolean = false,
    var acceptAlunos: Boolean = false,
    var price: Float = 0.0f,
    var status: Boolean = false
)
