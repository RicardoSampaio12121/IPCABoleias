package com.example.ipcaboleias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.chat.RVChatMessagesAdapter
import com.example.ipcaboleias.chat.TextMessage
import com.example.ipcaboleias.firebaseRepository.ChatRepository
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
    private lateinit var messagesListenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        channelId = intent.getStringExtra("channelId")!!

        // Carregar mensagens
        messagesListenerRegistration =
            chatRepo.addChatMessagesListener(channelId, this, this::updateRecyclerView)

        //adapter = RVChatMessagesAdapter()

        val tvOptions = findViewById<TextView>(R.id.textViewOptions)
        val sendMessageButton = findViewById<CircleImageView>(R.id.sendMessage)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val channelId = intent.getStringExtra("channelId")



        sendMessageButton.setOnClickListener {

            var message = TextMessage(
                etMessage.text.toString(),
                Calendar.getInstance().time,
                FirebaseAuth.getInstance().currentUser!!.uid
            )

            etMessage.setText("")
            chatRepo.SendMessage(message, channelId!!)
        }


        tvOptions.setOnClickListener {
            val popUpMenu = PopupMenu(this, tvOptions)
            popUpMenu.inflate(R.menu.my_active_rides_menu)

            popUpMenu.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when(item?.itemId) {
                        R.id.edit -> {
                            //Fazer nada
                        }
                    }
                    return false
                }
            })
            popUpMenu.show()
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