package com.example.ipcaboleias

data class Reserve(
    val id: String = "",
    val pubId: String = "",
    var approved: Boolean = false,
    val startLatitude: Double = 0.0,
    val startLongitude: Double = 0.0,
    var uid: String = "",
    var ownerUid: String = ""
)
