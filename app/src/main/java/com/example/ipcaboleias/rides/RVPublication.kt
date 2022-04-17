package com.example.ipcaboleias.rides

import java.util.*

data class RVPublication(
    val name: String,
    val picture: String,
    val car : String?,
    val from: String,
    val to: String,
    val date: String,
    val amount: Double?,
    val places: Int?
)
