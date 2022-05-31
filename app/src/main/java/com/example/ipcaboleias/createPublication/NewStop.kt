package com.example.ipcaboleias.createPublication

data class NewStop (val latitude: Double, val longitude: Double){
    constructor(): this(0.0, 0.0)
}