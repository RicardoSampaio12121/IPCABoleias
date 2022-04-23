package com.example.ipcaboleias.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ipcaboleias.rides.Ride

class PublicationDetailsViewModel : ViewModel() {
    val ride = MutableLiveData<Ride>()

    fun setRide(newRide: Ride){
        ride.value = newRide
    }

    fun getRide() : Ride{
        return ride.value!!
    }

}