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
import com.example.ipcaboleias.rides.PossibleStopMapVisualizerFragment
import com.google.android.gms.maps.model.LatLng

private const val Id = "id"
private const val EndLatitude = "endLatitude"
private const val EndLongitude = "endLongitude"


class PassengersFragment : Fragment(R.layout.fragment_passengers) {

    private var _binding: FragmentPassengersBinding? = null
    private val binding get() = _binding!!

    private var _id: String? = null
    private var _endLatitude: Double? = null
    private var _endLongitude: Double? = null

    private val POSSIBLE_STOP_MAP_VISUALIZER_FRAG_TAG = "possibleStopMapVisualizerFragTag"

    private lateinit var pubRepo: PublicationsRepository
    private lateinit var usersRepo: UsersRepository
    private lateinit var adapter: RVPassengersAdapter

    private val passengers: MutableList<PassangerPresentation> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _id = it.getString(Id)
            _endLatitude = it.getString(EndLatitude)!!.toDouble()
            _endLongitude = it.getString(EndLongitude)!!.toDouble()
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
        adapter =
            RVPassengersAdapter(
                passengers,
                LatLng(_endLatitude!!, _endLongitude!!),
                object : RVPassengersAdapter.ChatButtonClickListener {
                    override fun onChatButtonClickListener(position: Int) {
                        usersRepo.getOrCreateChatChannel(passengers[position].uid) {
                            val intent = Intent(requireActivity(), ChatActivity::class.java)
                            intent.putExtra("channelId", it)
                            requireActivity().startActivity(intent)
                        }

                    }
                },
                object : RVPassengersAdapter.OpenMapClickListener {
                    override fun onOpenMapClickListener(position: Int) {
                        requireActivity().supportFragmentManager.beginTransaction().add(
                            R.id.frameFragment,
                            PossibleStopMapVisualizerFragment.newInstance(
                                LatLng(
                                    passengers[position].startLatitude,
                                    passengers[position].startLongitude
                                ),
                            ), POSSIBLE_STOP_MAP_VISUALIZER_FRAG_TAG
                        ).commit()
                    }
                })
        RVPassengers.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(id: String, endLatitude: String, endLongitude: String) =
            PassengersFragment().apply {
                arguments = Bundle().apply {
                    putString(Id, id)
                    putString(EndLatitude, endLatitude)
                    putString(EndLongitude, endLongitude)
                }
            }
    }
}