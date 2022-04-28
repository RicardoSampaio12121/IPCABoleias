package com.example.ipcaboleias.firebaseRepository

import com.example.ipcaboleias.chat.TextMessage
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatRepository {

    fun SendMessage(message : TextMessage, channelId : String){
        val db = Firebase.firestore

        db.collection("chatChannels")
            .document(channelId)
            .collection("messages")
            .add(message)
    }
}