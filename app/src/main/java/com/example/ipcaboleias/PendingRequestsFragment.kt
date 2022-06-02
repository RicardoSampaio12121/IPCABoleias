package com.example.ipcaboleias

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.FragmentPendingRequestsBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.User
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.example.ipcaboleias.history.RVPendingRequestsAdapter
import com.example.ipcaboleias.registration.NewUser
import com.example.ipcaboleias.rides.Ride
import com.google.android.gms.maps.model.LatLng

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

        binding.apply {
            usersRepo.getReservesToBeApproved { reserve ->
                var iterator = 0

                for (doc in reserve) {
                    iterator++
                    pubRepo.getPublicationById(doc.pubId) { ride ->
                        usersRepo.getUser(doc.uid) { user ->
                            reservePresentations.add(
                                newReservePresentation(
                                    doc,
                                    user,
                                    ride,
                                    doc.uid
                                )
                            )

                            if (iterator + 1 > reserve.size) {
                                startRecyclerView()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun newReservePresentation(
        reserve: Reserve,
        user: User,
        ride: Ride,
        passengerId: String
    ): ReservePresentation {
        return ReservePresentation(
            reserve.pubId,
            passengerId,
            "${user.name} ${user.surname}",
            ride.acceptDoc,
            reserve.startLatitude,
            reserve.startLongitude,
            ride.endLatitute,
            ride.endLongitude,
            ride.dateTime,
            user.profilePicture!!
        )
    }

    private fun startRecyclerView() {
        val rvPendingReserve = requireActivity().findViewById<RecyclerView>(R.id.rvPendingRequests)
        val supportFragmentManager = requireActivity().supportFragmentManager

        rvPendingReserve.layoutManager = LinearLayoutManager(activity)
        adapter = RVPendingRequestsAdapter(
            reservePresentations,
            object : RVPendingRequestsAdapter.AcceptButtonClickListener {
                override fun onAcceptButtonClickListener(position: Int) {
                    pubRepo = PublicationsRepository(requireContext())
                    usersRepo = UsersRepository(requireContext())

                    pubRepo.addPassenger(
                        reservePresentations[position].docId,
                        reservePresentations[position].passengerId,
                        LatLng(
                            reservePresentations[position].startLat,
                            reservePresentations[position].startLong
                        )
                    )

                    pubRepo.removeSeat(reservePresentations[position].docId)

                    usersRepo.acceptPassenger(
                        reservePresentations[position].docId,
                        reservePresentations[position].passengerId
                    )

                    adapter.removeItem(position)

                    Toast.makeText(
                        requireContext(),
                        "Passageiro adicionado com sucesso",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }, object : RVPendingRequestsAdapter.ChatButtonClickListener {
                override fun onChatButtonClickListener(position: Int) {
                    usersRepo.getOrCreateChatChannel(reservePresentations[position].passengerId) {
                        val intent = Intent(requireActivity(), ChatActivity::class.java)
                        intent.putExtra("channelId", it)
                        requireActivity().startActivity(intent)
                    }
                }
            })


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