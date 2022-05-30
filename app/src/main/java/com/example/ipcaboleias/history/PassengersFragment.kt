package com.example.ipcaboleias.history

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.ChatActivity
import com.example.ipcaboleias.R
import com.example.ipcaboleias.databinding.FragmentPassengersBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.User
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.example.ipcaboleias.registration.NewUser

private const val Id = "id"

class PassengersFragment : Fragment(R.layout.fragment_passengers) {

    private var _binding: FragmentPassengersBinding? = null
    private val binding get() = _binding!!

    private var _id: String? = null

    private lateinit var pubRepo: PublicationsRepository
    private lateinit var usersRepo: UsersRepository
    private lateinit var adapter: RVPassengersAdapter

    private val passengers: MutableList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _id = it.getString(Id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startAdapter()

        _binding = FragmentPassengersBinding.bind(view)
        pubRepo = PublicationsRepository(requireContext())
        val supportFragmentManager = requireActivity().supportFragmentManager

        binding.apply {
            pubRepo.getPublicationPassengersById(_id!!) {
                passengers.add(it)

                adapter.notifyItemInserted(passengers.size - 1)
            }

            returnButton.setOnClickListener {
                supportFragmentManager.beginTransaction().remove(this@PassengersFragment).commit()
            }
        }
    }

    private fun startAdapter() {
        val RVPassengers = requireActivity().findViewById<RecyclerView>(R.id.RVPassengers)
        usersRepo = UsersRepository(requireContext())

        RVPassengers.layoutManager = LinearLayoutManager(activity)
        adapter = RVPassengersAdapter(passengers, object: RVPassengersAdapter.ChatButtonClickListener{
            override fun onChatButtonClickListener(position: Int) {
                usersRepo.getOrCreateChatChannel(passengers[position].uid) {
                    val intent = Intent(requireActivity(), ChatActivity::class.java)
                    intent.putExtra("channelId", it)
                    requireActivity().startActivity(intent)
                }

            }
        })
        RVPassengers.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            PassengersFragment().apply {
                arguments = Bundle().apply {
                    putString(Id, id)
                }
            }
    }
}