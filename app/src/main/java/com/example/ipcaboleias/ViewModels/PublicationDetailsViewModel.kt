package com.example.ipcaboleias.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ipcaboleias.rides.Ride
import com.example.ipcaboleias.rides.RidePresentation

class PublicationDetailsViewModel : ViewModel() {
    val ride = MutableLiveData<RidePresentation>()

    fun setRide(newRide: RidePresentation){
        ride.value = newRide
    }

    fun getRide() : RidePresentation{
        return ride.value!!
    }

}