package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.FragmentPendingRequestsBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.example.ipcaboleias.registration.NewUser
import com.example.ipcaboleias.rides.Ride

class PendingRequestsFragment : Fragment(R.layout.fragment_pending_requests) {
    private var _binding: FragmentPendingRequestsBinding? = null
    private val binding get() = _binding!!

    private lateinit var pubRepo: PublicationsRepository
    private lateinit var usersRepo: UsersRepository
    private lateinit var adapter: RVPendingRequestsAdapter

    private var reservePresentations: MutableList<ReservePresentation> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPendingRequestsBinding.bind(view)
        usersRepo = UsersRepository(requireContext())
        pubRepo = PublicationsRepository(requireContext())

        adapter = RVPendingRequestsAdapter(reservePresentations)

        binding.apply {
            usersRepo.getReservesToBeApproved {
                var iterator = 0

                for (doc in it) {
                    println(doc.id)
                    iterator++
                    pubRepo.getPublicationById(doc.id) { ride ->
                        usersRepo.getUser(doc.uid) { user ->
                            reservePresentations.add(newReservePresentation(user, ride))

                            if (iterator + 1 > it.size) {
                                startRecyclerView()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun newReservePresentation(user: NewUser, ride: Ride): ReservePresentation {
        return ReservePresentation(
            "${user.name} ${user.surname}",
            ride.acceptDoc,
            ride.startLatitute,
            ride.startLongitude,
            ride.endLatitute,
            ride.endLongitude,
            ride.date,
            ride.time,
            user.profilePicture!!
        )
    }

    private fun startRecyclerView() {
        val rvPendingReserve = requireActivity().findViewById<RecyclerView>(R.id.rvPendingRequests)

        rvPendingReserve.layoutManager = LinearLayoutManager(activity)
        adapter = RVPendingRequestsAdapter(reservePresentations)
        rvPendingReserve.adapter = adapter
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            PendingRequestsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}