package com.example.ipcaboleias

import androidx.lifecycle.MutableLiveData

class NewPublicationAsPassenger {
    var startLatitute : Double = 0.0
    var startLongitude : Double = 0.0
    var endLatitute : Double = 0.0
    var endLongitude : Double = 0.0
    var date : String = ""
    var time : String = ""
    var type : String = ""
}

class NewPublicationAsDriver {
    var startLatitute : Double = 0.0
    var startLongitude : Double = 0.0
    var endLatitute : Double = 0.0
    var endLongitude : Double = 0.0
    var date : String = ""
    var time : String = ""
    var type : String = ""
    var price : Float = 0.0f
}