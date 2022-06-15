package com.example.ipcaboleias.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.R
import com.example.ipcaboleias.databinding.FragmentMyRidesBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.example.ipcaboleias.rides.Ride


class MyRidesFragment : Fragment(R.layout.fragment_my_rides) {
    private var _binding: FragmentMyRidesBinding? = null
    private val binding get() = _binding!!

    private lateinit var pubRepo: PublicationsRepository
    private lateinit var usersRepo: UsersRepository

    private lateinit var myRides: MutableList<Ride>
    private lateinit var myRidesIds: MutableList<String>

    private lateinit var adapter: RVmyRidesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyRidesBinding.bind(view)
        pubRepo = PublicationsRepository(requireContext())
        usersRepo = UsersRepository(requireContext())

        myRides = ArrayList()
        myRidesIds = ArrayList()

        adapter = RVmyRidesAdapter(myRides)

        binding.apply {
            usersRepo.getCurrentUserPublicationsIds {
                val size = it.size
                var iterator = 0

                for (id in it) {
                    pubRepo.getPublicationById(id) { ride ->
                        iterator++
                        myRides.add(ride)
                        myRidesIds.add(id)

                        if (iterator + 1 > size) {
                            updateRecyclerView()
                        }
                    }
                }
            }
        }

    }

    private fun updateRecyclerView(){
        val rvMyRides = requireActivity().findViewById<RecyclerView>(R.id.rvMyRides)

        rvMyRides.layoutManager = LinearLayoutManager(activity)
        adapter = RVmyRidesAdapter(myRides)
        rvMyRides.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MyRidesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}