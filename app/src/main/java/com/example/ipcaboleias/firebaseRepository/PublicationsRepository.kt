package com.example.ipcaboleias.firebaseRepository

import android.content.Context
import com.example.ipcaboleias.NewPublicationAsDriver
import com.example.ipcaboleias.NewPublicationAsPassenger
import com.example.ipcaboleias.firebaseRepository.Callbacks.GetPublicationsCallback
import com.example.ipcaboleias.firebaseRepository.Callbacks.NewPublicationCallback
import com.example.ipcaboleias.registration.NewUser
import com.example.ipcaboleias.rides.Ride
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PublicationsRepository(private val context: Context) {

    fun createPublicationAsDriver(
        publication: NewPublicationAsDriver,
        myCallback: NewPublicationCallback
    ) {
        val database = Firebase.firestore

//        val pub = hashMapOf(
//            "uid" to publication.uid,
//            "startLatitude" to publication.startLatitute,
//            "startLongitude" to publication.startLongitude,
//            "endLatitude" to publication.endLatitute,
//            "endLongitude" to publication.endLongitude,
//            "date" to publication.date,
//            "time" to publication.time,
//            "type" to publication.type,
//            "places" to publication.places,
//            "description" to publication.description,
//            "uniqueRide" to publication.uniqueDrive,
//            "acceptDoc" to publication.acceptDoc,
//            "acceptAlunos" to publication.acceptAlunos,
//            "price" to publication.price
//        )

//        database.collection("publications").add(pub)
//            .addOnSuccessListener {
//                myCallback.onCallback(true)
//            }
//            .addOnFailureListener {
//                myCallback.onCallback(false)
//            }

        var uid = FirebaseAuth.getInstance().currentUser!!.uid

        //Adicionar publicação

        val newPub = database.collection("publications").document()
        newPub.set(publication)


        //Adicionar entrada de publicação no utilizador que a criou
        database.collection("users")
            .document(uid)
            .collection("publications")
            .document(newPub.id)
            .set(mapOf("nada" to "nada"))

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


    suspend fun getPublications(myCallback: GetPublicationsCallback) {
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