package com.example.ipcaboleias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.chat.RVChatMessagesAdapter
import com.example.ipcaboleias.chat.TextMessage
import com.example.ipcaboleias.firebaseRepository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import de.hdodenhof.circleimageview.CircleImageView
import java.time.LocalDateTime
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var channelId: String
    private lateinit var adapter: RVChatMessagesAdapter
    private val chatRepo = ChatRepository()
    private lateinit var messagesListenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        channelId = intent.getStringExtra("channelId")!!

        // Carregar mensagens
        messagesListenerRegistration =
            chatRepo.addChatMessagesListener(channelId, this, this::updateRecyclerView)

        //adapter = RVChatMessagesAdapter()


        val sendMessageButton = findViewById<CircleImageView>(R.id.sendMessage)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val channelId = intent.getStringExtra("channelId")



        sendMessageButton.setOnClickListener {

            var message = TextMessage(
                etMessage.text.toString(),
                Calendar.getInstance().time,
                FirebaseAuth.getInstance().currentUser!!.uid
            )

            chatRepo.SendMessage(message, channelId!!)
        }


    }

    private fun updateRecyclerView(messages: MutableList<TextMessage>) {
        val recycler_view_mesasges = findViewById<RecyclerView>(R.id.recyclerViewMessages)

        fun init() {
            recycler_view_mesasges.layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = RVChatMessagesAdapter(messages)
            recycler_view_mesasges.adapter = adapter
        }

        init()
    }
}