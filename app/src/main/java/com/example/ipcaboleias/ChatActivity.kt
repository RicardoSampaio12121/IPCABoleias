package com.example.ipcaboleias

import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.chat.RVChatMessagesAdapter
import com.example.ipcaboleias.chat.TextMessage
import com.example.ipcaboleias.firebaseRepository.ChatRepository
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.example.ipcaboleias.registration.NewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var channelId: String
    private lateinit var adapter: RVChatMessagesAdapter
    private val chatRepo = ChatRepository()
    private val usersRepo = UsersRepository(this)
    private lateinit var messagesListenerRegistration: ListenerRegistration

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        channelId = intent.getStringExtra("channelId")!!

        usersRepo.getUserFromChatChannel(channelId) {
            fillFields(it)
        }

        // Carregar mensagens
        messagesListenerRegistration =
            chatRepo.addChatMessagesListener(channelId, this, this::updateRecyclerView)

        //adapter = RVChatMessagesAdapter()

        val returnButton = findViewById<AppCompatImageButton>(R.id.returnButton)
        val sendMessageButton = findViewById<CircleImageView>(R.id.sendMessage)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val channelId = intent.getStringExtra("channelId")

        sendMessageButton.setOnClickListener {
            val message = TextMessage(
                etMessage.text.toString(),
                Calendar.getInstance().time,
                FirebaseAuth.getInstance().currentUser!!.uid
            )

            etMessage.setText("")
            chatRepo.SendMessage(message, channelId!!)
        }

        returnButton.setOnClickListener {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillFields(user: NewUser) {
        val profilePic = this.findViewById<CircleImageView>(R.id.profilePic)
        val tvName = this.findViewById<TextView>(R.id.personName)

        val byteArray: ByteArray =
            Base64.getDecoder().decode(user.profilePicture)
        val bitMapPic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        profilePic.setImageBitmap(bitMapPic)

        tvName.text = "${user.name} ${user.surname}"
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