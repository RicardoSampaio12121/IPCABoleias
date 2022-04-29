package com.example.ipcaboleias.firebaseRepository

import android.content.Context
import android.util.Log
import com.example.ipcaboleias.chat.TextMessage
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class ChatRepository {

    fun SendMessage(message: TextMessage, channelId: String) {
        val db = Firebase.firestore

        db.collection("chatChannels")
            .document(channelId)
            .collection("messages")
            .add(message)
    }

    fun addChatMessagesListener(
        channelId: String,
        context: Context,
        onListen: (MutableList<TextMessage>) -> Unit
    ): ListenerRegistration {
        val db = Firebase.firestore

        return db.collection("chatChannels")
            .document(channelId)
            .collection("messages")
            .orderBy("time")
            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<TextMessage>()
                querySnapshot!!.documents.forEach {
                    items.add(
                        TextMessage(
                            it["message"].toString(),
                            Calendar.getInstance().time,
                            it["senderId"].toString()
                        )
                    )
                    return@forEach
                }
                onListen(items)
            }
    }
}