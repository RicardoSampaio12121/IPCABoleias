package com.example.ipcaboleias.firebaseRepository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.ipcaboleias.NewPublicationAsDriver
import com.example.ipcaboleias.NewPublicationAsPassenger
import com.example.ipcaboleias.firebaseRepository.Callbacks.NewPublicationCallback
import com.example.ipcaboleias.rides.Ride
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.DateTime
import java.sql.Date
import java.sql.Time
import java.sql.Time.valueOf
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset


class PublicationsRepository(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun createPublicationAsDriver(
        publication: NewPublicationAsDriver,
        myCallback: NewPublicationCallback
    ) {
        val database = Firebase.firestore

        val toInsert = PublicationAsDriverToPublicationDriverFirestore(publication)

        var uid = FirebaseAuth.getInstance().currentUser!!.uid

        //Adicionar publicação

        val newPub = database
            .collection("publications")
            .document()

        newPub.set(toInsert)

        //Adicionar entrada de publicação no utilizador que a criou
        database.collection("users")
            .document(uid)
            .collection("publications")
            .document(newPub.id)
            .set(mapOf("status" to true, "type" to "Driver"))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createPublicationAsPassenger(
        publication: NewPublicationAsPassenger,
        onComplete: (checker: Boolean) -> Unit
    ) {
        val database = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val toInsert = PublicationAsPassengerToPublicationPassengerFirestore(publication)


        val newPub = database.collection("publications")
            .document()

        newPub.set(toInsert)

        database.collection("users")
            .document(uid)
            .collection("publications")
            .document(newPub.id)
            .set(mapOf("status" to true, "type" to "Passenger"))

        onComplete(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun PublicationAsDriverToPublicationDriverFirestore(pub: NewPublicationAsDriver): NewPublicationDriverFirestore {

        val dateTime = LocalDateTime.of(pub.date, pub.time)
        val seconds = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond()
        val nanos = dateTime.nano

        return NewPublicationDriverFirestore(
            pub.uid,
            pub.startLatitute,
            pub.startLongitude,
            pub.endLatitute,
            pub.endLongitude,
            Timestamp(seconds, nanos),
            pub.type,
            pub.places,
            pub.description,
            pub.uniqueDrive,
            pub.acceptDoc,
            pub.acceptAlunos,
            pub.price,
            pub.status,
            false
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun PublicationAsPassengerToPublicationPassengerFirestore(pub: NewPublicationAsPassenger): NewPublicationPassengerFirestore {

        val dateTime = LocalDateTime.of(pub.date, pub.time)
        val seconds = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond()
        val nanos = dateTime.nano

        return NewPublicationPassengerFirestore(
            pub.uid,
            pub.startLatitute,
            pub.startLongitude,
            pub.endLatitute,
            pub.endLongitude,
            Timestamp(seconds, nanos),
            pub.type,
            pub.acceptDoc,
            pub.acceptAlunos,
            pub.status,
            false
        )
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
            .whereEqualTo("full", false)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    list.add(document.toObject(Ride::class.java))
                }
                onComplete(list)
            }
    }

    fun getPublicationByIdWithDocId(id: String, onComplete: (ride: RidesWithDocId) -> Unit) {
        val db = Firebase.firestore

        db.collection("publications")
            .document(id)
            .get()
            .addOnSuccessListener {
                val r = it.toObject(RidesWithDocId::class.java)
                r!!.docId = it.id
                onComplete(r)
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
        date: Timestamp,
        onComplete: (checker: Boolean) -> Unit
    ) {
        val db = Firebase.firestore

        db.collection("publications")
            .document(id)
            .update(mapOf("dateTime" to date))
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
        dateTime: Timestamp,
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
                .whereEqualTo("dateTime", dateTime)
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

        db.collection("users")
            .document(passengerId)
            .collection("scheduledRides")
            .document(docId)
            .set(mapOf("rideId" to docId))
    }

    fun removeSeat(docId: String) {
        val db = Firebase.firestore

        db.collection("publications")
            .document(docId)
            .get()
            .addOnSuccessListener {
                var newPlaces = it["places"].toString().toInt() - 1

                if (newPlaces == 0) {
                    db.collection("publications")
                        .document(docId)
                        .update(mapOf("places" to newPlaces, "full" to true))
                } else {
                    db.collection("publications")
                        .document(docId)
                        .update(mapOf("places" to newPlaces))
                }
            }
    }

    fun getCurrentUserActiveRidesAsPassenger(onComplete: (output: Ride) -> Unit) {
        val db = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("users")
            .document(uid)
            .collection("scheduledRides")
            .get()
            .addOnSuccessListener {
                it.documents.forEach { document ->
                    getPublicationById(document.id){ ride ->
                        onComplete(ride)
                    }
                }
            }
    }

    fun getPublicationPassengersById(docId: String, onComplete: (output: User) -> Unit) {
        val db = Firebase.firestore

        db.collection("publications")
            .document(docId)
            .collection("passengers")
            .get()
            .addOnSuccessListener { passengers ->
                for (passenger in passengers) {
                    db.collection("users")
                        .document(passenger.id)
                        .get()
                        .addOnSuccessListener { user ->
                            var u = user.toObject(User::class.java)!!
                            u.uid = user.id

                            onComplete(u)
                        }
                }
            }
    }
}