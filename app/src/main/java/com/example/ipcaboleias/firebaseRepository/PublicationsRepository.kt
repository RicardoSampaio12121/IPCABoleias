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

        var uid = FirebaseAuth.getInstance().currentUser!!.uid

        //Adicionar publicação

        val newPub = database
            .collection("publications")
            .document()

        newPub.set(publication)

        //Adicionar entrada de publicação no utilizador que a criou
        database.collection("users")
            .document(uid)
            .collection("publications")
            .document(newPub.id)
            .set(mapOf("status" to true))

    }

    fun createPublicationAsPassenger(
        publication: NewPublicationAsPassenger,
        onComplete: (checker: Boolean) -> Unit
    ) {
        val database = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val newPub = database.collection("publications")
            .document()

        newPub.set(publication)

        database.collection("users")
            .document(uid)
            .collection("publications")
            .document(newPub.id)
            .set(mapOf("status" to true))

        onComplete(true)
    }


//    suspend fun getPublications(myCallback: GetPublicationsCallback) {
//        val db = Firebase.firestore
//        var list: MutableList<Ride> = ArrayList()
//
//        db.collection("publications")
//            .get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    for (document in task.result) {
//                        list.add(document.toObject(Ride::class.java))
//                    }
//                    myCallback.onCallback(list)
//                }
//            }
//    }

    fun getPublications(onComplete: (rides: List<Ride>) -> Unit) {
        val db = Firebase.firestore
        var list: MutableList<Ride> = ArrayList()

        db.collection("publications")
            .whereEqualTo("status", true)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    list.add(document.toObject(Ride::class.java))
                }
                onComplete(list)
            }
    }

    fun getPublicationById(id: String, onComplete: (ride: Ride) -> Unit) {
        val db = Firebase.firestore

        db.collection("publications")
            .document(id)
            .get()
            .addOnSuccessListener {
                onComplete(it.toObject(Ride::class.java)!!)
            }
    }

//    fun getPublicationsByIds(ids: List<String>, onComplete: (rides: List<Ride>) -> Unit){
//        val db = Firebase.firestore
//
//
//
//    }

    fun deactivateRide(id: String, onComplete: (checker: Boolean) -> Unit) {
        val db = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("publications")
            .document(id)
            .update(mapOf("status" to false))


        db.collection("users")
            .document(uid)
            .collection("publications")
            .document(id)
            .update(mapOf("status" to false))

        onComplete(true)
    }

    fun editDateTime(
        id: String,
        date: String,
        time: String,
        onComplete: (checker: Boolean) -> Unit
    ) {
        val db = Firebase.firestore

        db.collection("publications")
            .document(id)
            .update(mapOf("date" to date, "time" to time))
            .addOnSuccessListener {
                onComplete(true)
            }
    }

    fun editPrice(id: String, price: Float, onComplete: (checker: Boolean) -> Unit) {
        val db = Firebase.firestore

        db.collection("publications")
            .document(id)
            .update(mapOf("price" to price))
            .addOnSuccessListener {
                onComplete(true)
            }
    }

    fun editPlaces(id: String, places: Int, onComplete: (checker: Boolean) -> Unit) {
        val db = Firebase.firestore

        db.collection("publications")
            .document(id)
            .update(mapOf("places" to places))
            .addOnSuccessListener {
                onComplete(true)
            }
    }
}