package com.example.ipcaboleias

data class ReservePresentation(
    var name: String = "",
    var doc: Boolean = false,
    var startLat: Double = 0.0,
    var startLong: Double = 0.0,
    var endLat: Double = 0.0,
    var endLong: Double = 0.0,
    var date: String = "",
    var time: String = "",
    var profilePicture: String = ""
)
