package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.FragmentScheduledRidesBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
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

        binding.apply {
            //Buscar as minhas boleias agendadas

            pubRepo.getCurrentUserActiveRidesAsPassenger { rides ->
                val size = rides.size
                var iterator = 0
                println("Entra no pubRepo")
                println("Size: ${rides.size}")
                for (ride in rides) {
                    iterator++
                    println("Entra no for")
                    usersRepo.getUser(ride.uid) { user ->
                        scheduledRidesPresentations.add(newScheduledRidePresentations(ride, user))

                        if (iterator + 1 > size) {
                            println("Entra no if")
                            startAdapter()
                        }
                    }
                }
            }
        }
    }

    private fun newScheduledRidePresentations(
        ride: Ride,
        user: NewUser
    ): ScheduledRidePresentation {
        return ScheduledRidePresentation(
            ride.uid,
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