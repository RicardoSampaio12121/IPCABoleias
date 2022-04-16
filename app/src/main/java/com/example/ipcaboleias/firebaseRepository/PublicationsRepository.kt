package com.example.ipcaboleias.firebaseRepository

import android.content.Context
import android.widget.Toast
import com.example.ipcaboleias.NewPublicationAsDriver
import com.example.ipcaboleias.NewPublicationAsPassenger
import com.example.ipcaboleias.firebaseRepository.Callbacks.NewPublicationCallback
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PublicationsRepository(private val context: Context) {

    fun createPublicationAsDriver(publication : NewPublicationAsDriver, myCallback : NewPublicationCallback){
        val database = Firebase.firestore

        val pub = hashMapOf(
            "uid" to publication.uid,
            "startLatitude" to publication.startLatitute,
            "startLongitude" to publication.startLongitude,
            "endLatitude" to publication.endLatitute,
            "endLongitude" to publication.endLongitude,
            "date" to publication.date,
            "time" to publication.time,
            "type" to publication.type,
            "places" to publication.places,
            "price" to publication.price
        )

        database.collection("publications").add(pub)
            .addOnSuccessListener {
                myCallback.onCallback(true)
            }
            .addOnFailureListener {
                myCallback.onCallback(false)
            }
    }

    fun createPublicationAsPassenger(publication : NewPublicationAsPassenger, myCallback: NewPublicationCallback){
        val database = Firebase.firestore

        val pub = hashMapOf(
            "uid" to publication.uid,
            "startLatitude" to publication.startLatitute,
            "startLongitude" to publication.startLongitude,
            "endLatitude" to publication.endLatitute,
            "endLongitude" to publication.endLongitude,
            "date" to publication.date,
            "time" to publication.time,
            "type" to publication.type
        )

        database.collection("publications").add(pub)
            .addOnSuccessListener {
                myCallback.onCallback(true)
            }
            .addOnFailureListener {
                myCallback.onCallback(false)
            }
    }
}