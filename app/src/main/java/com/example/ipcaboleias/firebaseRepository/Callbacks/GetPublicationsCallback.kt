package com.example.ipcaboleias.firebaseRepository.Callbacks

import com.example.ipcaboleias.rides.Ride

interface GetPublicationsCallback {
    fun onCallback(rides:MutableList<Ride>)
}