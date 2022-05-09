package com.example.ipcaboleias.firebaseRepository

import android.content.Context
import android.widget.Toast
import com.example.ipcaboleias.chat.ChatChannel
import com.example.ipcaboleias.firebaseRepository.Callbacks.ChatChannelIdCallBack
import com.example.ipcaboleias.firebaseRepository.Callbacks.UserCallback
import com.example.ipcaboleias.firebaseRepository.Callbacks.userLoginCallback
import com.example.ipcaboleias.registration.NewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UsersRepository(private val context: Context) {

    private lateinit var auth: FirebaseAuth

    fun userRegistration(user: NewUser, password: String) {
        auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val firebaseUser: FirebaseUser = task.result!!.user!!

                firebaseUser.sendEmailVerification().addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Enviamos um email de confirmação para o seu endereço",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Falha ao enviar email de verificação para " + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Guardar dados
                val uid = firebaseUser.uid
                val db = Firebase.firestore

                val user = hashMapOf(
                    "name" to user.name,
                    "surname" to user.surname,
                    "email" to user.email,
                    "profilePicture" to user.profilePicture,
                    "carBrand" to user.carBrand,
                    "carModel" to user.carModel,
                    "carColor" to user.carColor,
                    "carPlate" to user.carPlate,
                    "carVerified" to false
                )

                db.collection("users").document(uid)
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Registo efetuado com sucesso", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Erro ao efetuar o registo, ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

            }
        }

    }

    fun userLogin(email: String, password: String, myCallBack: userLoginCallback) {

        var uid: String? = null

        auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user

                if (user!!.isEmailVerified) {
                    // Retornar o uid

                    uid = user.uid
                } else {
                    Toast.makeText(
                        context,
                        "Por favor, verifique o seu email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                myCallBack.onCallback(user.uid)

            }
        }.addOnFailureListener {
            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun getUser(uid: String, onComplete: (user: NewUser) -> Unit) {
        val db = Firebase.firestore

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                onComplete(it.toObject(NewUser::class.java)!!)
            }
    }

    fun getCurrentUser(onComplete: (user: NewUser) -> Unit) {
        val db = Firebase.firestore

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                onComplete(it.toObject(NewUser::class.java)!!)
            }
    }

    fun getCurrentUserActivePublicationsIds(onComplete: (publications: List<String>) -> Unit){
        val db = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val publications : MutableList<String> = ArrayList()

        db.collection("users")
            .document(uid)
            .collection("publications")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    if(doc["status"] == true) {
                        publications.add(doc.id)
                    }
                }
                onComplete(publications)
            }
    }

    fun getOrCreateChatChannel( //otherUserUid -> Ricardo
        otherUserUid: String,
        onComplete: (channelId: String) -> Unit
    ) {
        val db = Firebase.firestore
        val userUid = FirebaseAuth.getInstance().currentUser!!.uid

        println("UserId: $userUid")
        println("otherUserUid: $otherUserUid")

        db.collection("users")
            .document(userUid)
            .collection("engagedChatChannels")
            .document(otherUserUid)
            .get()
            .addOnSuccessListener {

                if (it.exists()) {
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }

                val newChannel = db.collection("chatChannels").document()
                newChannel.set(ChatChannel(mutableListOf(userUid, otherUserUid)))

                db.collection("users")
                    .document(userUid)
                    .collection("engagedChatChannels")
                    .document(otherUserUid)
                    .set(mapOf("channelId" to newChannel.id))

                db.collection("users")
                    .document(otherUserUid)
                    .collection("engagedChatChannels")
                    .document(userUid)
                    .set(mapOf("channelId" to newChannel.id))

                onComplete(newChannel.id)
            }
    }
}