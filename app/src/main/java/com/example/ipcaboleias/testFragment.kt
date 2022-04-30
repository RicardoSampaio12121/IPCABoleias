package com.example.ipcaboleias

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.chat.Channel
import com.example.ipcaboleias.chat.RVChatChannelsAdapter
import com.example.ipcaboleias.firebaseRepository.Callbacks.ChatChannelsIdsCallBack
import com.example.ipcaboleias.firebaseRepository.ChatRepository
import com.google.firebase.firestore.ListenerRegistration


class testFragment : Fragment() {

    private lateinit var chatChannelsListenerRegistration: ListenerRegistration
    private lateinit var adapter: RVChatChannelsAdapter
    private val chatRepo = ChatRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        chatChannelsListenerRegistration =
            chatRepo.addChatChannelsListener(requireContext(), this::updateRecyclerView)

        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        adapter.setOnItemClickListener(object : RVChatChannelsAdapter.onItemClickListener {
//            override fun onItemClick(position: Int) {
//                val intent = Intent(requireActivity(), ChatActivity::class.java)
//                intent.putExtra("channelId", "EhqYQ8OCeGLd9p3aguZI")
//                requireActivity().startActivity(intent)
//            }
//        })


//        chatRepo.GetChatChannels(requireContext(), object : ChatChannelsIdsCallBack {
//            override fun onCallback(channels: MutableList<String>) {
//                var list: MutableList<Channel> = ArrayList()
//
//                for (channel in channels) {
//                    list.add(Channel(channel))
//                }
//
//                val recyclerViewChannels =
//                    requireActivity().findViewById<RecyclerView>(R.id.recyclerViewChannels)
//
//                recyclerViewChannels.layoutManager = LinearLayoutManager(requireContext())
//                adapter = RVChatChannelsAdapter(list)
//                recyclerViewChannels.adapter = adapter
//
//                adapter.setOnItemClickListener(object : RVChatChannelsAdapter.onItemClickListener {
//                    override fun onItemClick(position: Int) {
//                        val intent = Intent(requireActivity(), ChatActivity::class.java)
//                        intent.putExtra("channelId", list[position].channelId)
//                        requireActivity().startActivity(intent)
//                    }
//                })
//
//            }
//        })

    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            testFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun updateRecyclerView(channels: MutableList<Channel>) {
        val recyclerViewChannels =
            requireActivity().findViewById<RecyclerView>(R.id.recyclerViewChannels)

        fun init() {
            recyclerViewChannels.layoutManager = LinearLayoutManager(requireContext())
            adapter = RVChatChannelsAdapter(channels)
            recyclerViewChannels.adapter = adapter

            adapter.setOnItemClickListener(object : RVChatChannelsAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val intent = Intent(requireActivity(), ChatActivity::class.java)
                    intent.putExtra("channelId", channels[position].channelId)
                    requireActivity().startActivity(intent)
                }
            })
        }
        init()
    }
}