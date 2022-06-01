package com.example.ipcaboleias.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterResultsViewModel: ViewModel() {
    val buttonClicked = MutableLiveData<Boolean>()

    val toLatitude = MutableLiveData<Double>()

    val seeDriversRides = MutableLiveData<Boolean>()
    val seePassengersRides = MutableLiveData<Boolean>()

    val acceptStudents = MutableLiveData<Boolean>()
    val acceptProfessors = MutableLiveData<Boolean>()


}