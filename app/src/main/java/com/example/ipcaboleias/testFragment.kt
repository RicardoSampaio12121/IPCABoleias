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
import com.example.ipcaboleias.databinding.FragmentTestBinding
import com.example.ipcaboleias.firebaseRepository.Callbacks.ChatChannelsIdsCallBack
import com.example.ipcaboleias.firebaseRepository.ChatRepository
import com.google.firebase.firestore.ListenerRegistration


class testFragment : Fragment(R.layout.fragment_test) {

    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentTestBinding.bind(view)

        binding.apply {

        }

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