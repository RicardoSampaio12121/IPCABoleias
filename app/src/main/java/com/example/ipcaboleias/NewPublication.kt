package com.example.ipcaboleias

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.ipcaboleias.createPublication.NewStop
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class NewPublicationAsPassenger {
    var uid : String = ""
    var startLatitute : Double = 0.0
    var startLongitude : Double = 0.0
    var endLatitute : Double = 0.0
    var endLongitude : Double = 0.0
    @RequiresApi(Build.VERSION_CODES.O)
    var date : LocalDate = LocalDate.of(2000, 7, 24)
    @RequiresApi(Build.VERSION_CODES.O)
    var time : LocalTime = LocalTime.of(7,30)
    var type : String = ""
    var acceptDoc : Boolean = false
    var acceptAlunos : Boolean = false
    var status: Boolean = true
}

class NewPublicationAsDriver {
    var uid : String = ""
    var startLatitute : Double = 0.0
    var startLongitude : Double = 0.0
    var endLatitute : Double = 0.0
    var endLongitude : Double = 0.0
    @RequiresApi(Build.VERSION_CODES.O)
    var date : LocalDate = LocalDate.of(2000, 7, 24)
    @RequiresApi(Build.VERSION_CODES.O)
    var time : LocalTime = LocalTime.of(7,30)
    var type : String = ""
    var places : Int = 1
    var description : String = ""
    var acceptDoc : Boolean = false
    var acceptAlunos : Boolean = false
    var price : Float = 0.0f
    var status: Boolean = true
    var stops: MutableList<NewStop> = ArrayList()
}