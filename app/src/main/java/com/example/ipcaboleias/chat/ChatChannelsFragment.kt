package com.example.ipcaboleias.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

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
