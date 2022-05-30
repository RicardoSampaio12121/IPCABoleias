package com.example.ipcaboleias.createPublication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PickStopViewModel: ViewModel() {
    val latitude = MutableLiveData<Double>()
    val longitude = MutableLiveData<Double>()
    val buttonClicked = MutableLiveData<Boolean>()
}