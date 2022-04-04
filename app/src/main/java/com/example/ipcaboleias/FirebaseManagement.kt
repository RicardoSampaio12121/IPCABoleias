package com.example.ipcaboleias

import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class FirebaseManagement {

    companion object {
        fun GetUserData(uid: String): NewUser {
            val database = FirebaseDatabase.getInstance().getReference("users")
            val user: NewUser = NewUser()

            database.child(uid).get()
                .addOnSuccessListener {
                    user.name = "olavalete"
                }
                .addOnFailureListener {
                    user.name = "omeunomeévanessa"
                }

//            database.child(uid).get().addOnSuccessListener {
//
//                if(it.exists()) {
//                    user.name = "entrou aqui"
//
//                    user.name = it.child("name")?.value.toString()
//                    user.surname = it.child("surname")?.value.toString()
//                    user.email = it.child("email")?.value.toString()
//                    user.carBrand = it.child("carBrand")?.value.toString()
//                    user.carModel = it.child("carModel")?.value.toString()
//                    user.carColor = it.child("carColor")?.value.toString()
//                    user.profilePicture = it.child("profilePicture")?.value.toString()
//                }
//                else{
//                    user.name = "deu merda"
//                }
//
//            }
//                .addOnFailureListener {
//                    user.name = "falhou miseravelmente"
//                }
            return user
        }
    }
}