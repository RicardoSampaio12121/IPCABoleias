package com.example.ipcaboleias.firebaseRepository

import android.content.Context
import com.example.ipcaboleias.NewPublicationAsDriver
import com.example.ipcaboleias.NewPublicationAsPassenger
import com.example.ipcaboleias.firebaseRepository.Callbacks.GetPublicationsCallback
import com.example.ipcaboleias.firebaseRepository.Callbacks.NewPublicationCallback
import com.example.ipcaboleias.registration.NewUser
import com.example.ipcaboleias.rides.Ride
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder


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


    fun getPublications(myCallback : GetPublicationsCallback){

        val db = Firebase.firestore
        val list : MutableList<Ride> = ArrayList<Ride>()
        var single : Ride
        var user : NewUser

        db.collection("publications")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    single = document.toObject<Ride>()

                    db.collection("users").document(single.uid).get().addOnSuccessListener { userResult ->

                        user = userResult.toObject<NewUser>()!!

                        single.name = "${user.name} ${user.surname}"
                        single.car = "${user.carBrand} ${user.carModel}"
                        single.profilePicture = user.profilePicture!!

                        list.add(single)

                        myCallback.onCallback(list)
                    }
                }
            }
            .addOnFailureListener { exception ->
                //Log.w(TAG, "Error getting documents.", exception)
            }
    }
}