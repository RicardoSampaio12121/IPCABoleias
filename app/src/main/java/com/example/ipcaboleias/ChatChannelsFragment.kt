package com.example.ipcaboleias

import android.location.GnssAntennaInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.chat.RVChatChannelsAdapter
import com.example.ipcaboleias.firebaseRepository.ChatRepository
import com.google.firebase.firestore.ListenerRegistration
import java.nio.channels.Channel

class ChatChannelsFragment : Fragment() {
//    private val chatRepo = ChatRepository()
//
//    private lateinit var chatChannelsListenerRegistration : ListenerRegistration
//    private lateinit var adapter : RVChatChannelsAdapter

    //chatChannelsListenerRegistration = chatRepo.addChatChannelsListener(requireContext(), this::updateRecyclerView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("Esta no onCreate")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("ESTA NO ONVIEWCREATED")

        //chatChannelsListenerRegistration = chatRepo.addChatChannelsListener(requireContext(), this::updateRecyclerView)

        println("ESTA NO ONVIEWCREATED")

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChatChannelsFragment().apply {
            }
    }


}
