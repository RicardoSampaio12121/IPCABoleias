package com.example.ipcaboleias.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectStartLocationViewModel : ViewModel() {
    val clickedButton = MutableLiveData<Boolean>()
    val startLatitude = MutableLiveData<Double>()
    val startLongitude = MutableLiveData<Double>()
}