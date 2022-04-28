package com.example.ipcaboleias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import com.example.ipcaboleias.chat.RVChatMessagesAdapter
import com.example.ipcaboleias.chat.TextMessage
import com.example.ipcaboleias.firebaseRepository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import java.time.LocalDateTime
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var channelId : String
    private lateinit var adapter : RVChatMessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Carregar mensagens



        adapter = RVChatMessagesAdapter()


        val sendMessageButton = findViewById<CircleImageView>(R.id.sendMessage)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val channelId = intent.getStringExtra("channelId")



        sendMessageButton.setOnClickListener {

            val chatRepo = ChatRepository()

            var message = TextMessage(etMessage.text.toString(), Calendar.getInstance().time, FirebaseAuth.getInstance().currentUser!!.uid)

            chatRepo.SendMessage(message, channelId!!)
        }

    }
}