package com.example.ipcaboleias.firebaseRepository

import android.content.Context
import com.example.ipcaboleias.NewPublicationAsDriver
import com.example.ipcaboleias.NewPublicationAsPassenger
import com.example.ipcaboleias.firebaseRepository.Callbacks.GetPublicationsCallback
import com.example.ipcaboleias.firebaseRepository.Callbacks.NewPublicationCallback
import com.example.ipcaboleias.registration.NewUser
import com.example.ipcaboleias.rides.Ride
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PublicationsRepository(private val context: Context) {

    fun createPublicationAsDriver(
        publication: NewPublicationAsDriver,
        myCallback: NewPublicationCallback
    ) {
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
            "description" to publication.description,
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

    fun createPublicationAsPassenger(
        publication: NewPublicationAsPassenger,
        myCallback: NewPublicationCallback
    ) {
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


    fun getPublications(myCallback: GetPublicationsCallback) {
        val db = Firebase.firestore
        var list: MutableList<Ride> = ArrayList()

        db.collection("publications")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        list.add(document.toObject(Ride::class.java))
                    }
                    myCallback.onCallback(list)
                }
            }
    }

}