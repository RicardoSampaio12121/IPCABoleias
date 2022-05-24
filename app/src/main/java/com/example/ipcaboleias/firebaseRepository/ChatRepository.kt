package com.example.ipcaboleias.firebaseRepository

import android.content.Context
import android.util.Log
import com.example.ipcaboleias.chat.Channel
import com.example.ipcaboleias.chat.ChatChannel
import com.example.ipcaboleias.chat.TextMessage
import com.example.ipcaboleias.firebaseRepository.Callbacks.ChatChannelsIdsCallBack
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.nio.channels.Channels
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class ChatRepository(private val context: Context) {

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

    fun addChatChannelsListener(
        context: Context,
        onListen: (Channel) -> Unit
    ): ListenerRegistration {
        val db = Firebase.firestore
        val usersRepo = UsersRepository(context)

        val userUid = FirebaseAuth.getInstance().currentUser!!.uid

        return db.collection("users")
            .document(userUid)
            .collection("engagedChatChannels")
            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatChannelsListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val channels = mutableListOf<Channel>()
                querySnapshot!!.documents.forEach {

                    onListen(Channel(it["channelId"].toString()))
                    return@forEach
                }
            }
    }

    fun GetChatChannels(context: Context, myCallback: ChatChannelsIdsCallBack) {
        val db = Firebase.firestore
        var channelsList: MutableList<String> = ArrayList()
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("users")
            .document(uid)
            .collection("engagedChatChannels")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                        channelsList.add(document["channelId"].toString())
                    }
                    myCallback.onCallback(channelsList)
                }
            }
    }


}