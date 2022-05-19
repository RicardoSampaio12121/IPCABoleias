package com.example.ipcaboleias.firebaseRepository

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.LocalTime

data class NewPublicationPassengerFirestore(
    var uid: String,
    var startLatitute: Double,
    var startLongitude: Double,
    var endLatitute: Double,
    var endLongitude: Double,
    var dateTime: Timestamp,
    var type: String,
    var acceptDoc: Boolean,
    var acceptAlunos: Boolean,
    var status: Boolean,
    var full: Boolean
)