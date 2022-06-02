package com.example.ipcaboleias.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.R
import com.example.ipcaboleias.databinding.FragmentScheduledRidesBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.User
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.example.ipcaboleias.registration.NewUser
import com.example.ipcaboleias.rides.Ride

class ScheduledRidesFragment : Fragment(R.layout.fragment_scheduled_rides) {
    private var _binding: FragmentScheduledRidesBinding? = null
    private val binding get() = _binding!!

    private lateinit var pubRepo: PublicationsRepository
    private lateinit var usersRepo: UsersRepository
    private lateinit var adapter: RVScheduledRidesAdapter

    private var scheduledRidesPresentations: MutableList<ScheduledRidePresentation> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScheduledRidesBinding.bind(view)
        pubRepo = PublicationsRepository(requireContext())
        usersRepo = UsersRepository(requireContext())

        startAdapter()

        binding.apply {
            //Buscar as minhas boleias agendadas

            pubRepo.getCurrentUserActiveRidesAsPassenger { ride ->
                println("UID: " + ride.uid)
                usersRepo.getUser(ride.uid) { user ->
                    scheduledRidesPresentations.add((newScheduledRidePresentations(ride, user)))
                    adapter.notifyItemInserted(scheduledRidesPresentations.size - 1)
                }
            }
        }
    }

    private fun newScheduledRidePresentations(
        ride: Ride,
        user: User
    ): ScheduledRidePresentation {
        return ScheduledRidePresentation(
            ride.uid,
            "${user.name} ${user.surname}",
            ride.acceptDoc,
            ride.startLatitute,
            ride.startLongitude,
            ride.endLatitute,
            ride.endLongitude,
            ride.dateTime,
            user.profilePicture!!
        )
    }

    private fun startAdapter() {
        val RVScheduledRides = requireActivity().findViewById<RecyclerView>(R.id.rvScheduledRides)

        RVScheduledRides.layoutManager = LinearLayoutManager(activity)
        adapter = RVScheduledRidesAdapter(scheduledRidesPresentations)
        RVScheduledRides.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ScheduledRidesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}