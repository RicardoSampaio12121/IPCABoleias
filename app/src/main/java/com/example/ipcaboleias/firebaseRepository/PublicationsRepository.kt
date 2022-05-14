package com.example.ipcaboleias.firebaseRepository

import android.content.Context
import android.util.Log
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

    fun reserveRide(
        date: String,
        time: String,
        uid: String,
        onComplete: (checker: Boolean) -> Unit
    ) {
        val db = Firebase.firestore
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

        if (currentUser == uid)
            onComplete(false)
        else {

            var doc = db.collection("publications")
                .whereEqualTo("uid", uid)
                .whereEqualTo("date", date)
                .whereEqualTo("time", time)
                .get()
                .addOnSuccessListener {
                    for (doc in it) {
                        db.collection("users")
                            .document(uid)
                            .collection("reserves")
                            .document(doc.id)
                            .set(mapOf("uid" to currentUser, "approved" to false))

                        db.collection("users")
                            .document(currentUser)
                            .collection("requests")
                            .document(doc.id)
                            .set(mapOf("uid" to currentUser, "approved" to false))

                        onComplete(true)
                    }
                }
        }
    }

    fun addReservesListener(onListen: (MutableList<String>) -> Unit) {
        val db = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("users")
            .document(uid)
            .collection("reserves")
            .whereEqualTo("approved", false)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "reserves error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val requests = mutableListOf<String>()
                val rides = mutableListOf<Ride>()

                querySnapshot!!.documents.forEach {
//                    db.collection("publications")
//                        .document(it.id)
//                        .get()
//                        .addOnSuccessListener { ride ->
//                            rides.add(ride.toObject(Ride::class.java)!!)
//                        }
                    requests.add(it["uid"].toString())
                }
                onListen(requests)
            }
    }

    fun addPassenger(docId: String, passengerId: String) {
        val db = Firebase.firestore

        db.collection("publications")
            .document(docId)
            .collection("passengers")
            .document(passengerId)
            .set(mapOf("uid" to passengerId))
    }

    fun removeSeat(docId: String){
        val db = Firebase.firestore

        db.collection("publications")
            .document(docId)
            .get()
            .addOnSuccessListener {
                var newPlaces = it["places"].toString().toInt() - 1

                db.collection("publications")
                    .document(docId)
                    .update(mapOf("places" to newPlaces))
            }
    }



}