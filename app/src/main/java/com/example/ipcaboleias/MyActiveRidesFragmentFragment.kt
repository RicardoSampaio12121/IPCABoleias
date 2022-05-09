package com.example.ipcaboleias

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.databinding.FragmentMyActiveRidesFragmentBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.example.ipcaboleias.history.RVmyActiveRidesAdapter
import com.example.ipcaboleias.registration.NewUser
import com.example.ipcaboleias.rides.Ride
import com.example.ipcaboleias.rides.RideDetailsFragment
import com.example.ipcaboleias.rides.RidePresentation


class MyActiveRidesFragmentFragment : Fragment(R.layout.fragment_my_active_rides_fragment) {
    private var _binding: FragmentMyActiveRidesFragmentBinding? = null
    private val binding get() = _binding!!

    private val RIDES_DETAILS_FRAG_TAG = "detailsFragTag"

    private val model: PublicationDetailsViewModel by activityViewModels()

    private lateinit var myRides: MutableList<Ride>

    private lateinit var pubRepo: PublicationsRepository
    private lateinit var usersRepo: UsersRepository
    private lateinit var adapter: RVmyActiveRidesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMyActiveRidesFragmentBinding.bind(view)
        pubRepo = PublicationsRepository(requireContext())
        myRides = ArrayList()

        adapter = RVmyActiveRidesAdapter(
            myRides,
            object : RVmyActiveRidesAdapter.OptionsMenuClickListener {
                override fun onOptionsMenuClicked(position: Int) {
                    menu(position)
                }
            })



        binding.apply {
            pubRepo.getCurrentUserActivePublications {
                for (ride in it) {
                    myRides.add(ride)
                }
                updateRecyclerView()
            }
        }

    }

    private fun updateRecyclerView() {
        val rvMyActiveRides =
            requireActivity().findViewById<RecyclerView>(R.id.rvMyActivePublications)

        rvMyActiveRides.layoutManager = LinearLayoutManager(activity)

        adapter = RVmyActiveRidesAdapter(
            myRides,
            object : RVmyActiveRidesAdapter.OptionsMenuClickListener {
                override fun onOptionsMenuClicked(position: Int) {
                    menu(position)
                }
            })

        rvMyActiveRides.adapter = adapter

        adapter.setOnItemClickListener(object : RVmyActiveRidesAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                var supportFragmentManager = requireActivity().supportFragmentManager

                usersRepo = UsersRepository(requireContext())
                usersRepo.getCurrentUser {
                    var ride = newRidePresentationObject(myRides[position], it)
                    model.setRide(ride)

                    supportFragmentManager.beginTransaction().add(
                        R.id.frameFragment,
                        RideDetailsFragment.newInstance(),
                        RIDES_DETAILS_FRAG_TAG
                    ).commit()
                }
            }
        })
    }

    private fun newRidePresentationObject(ride: Ride, user: NewUser): RidePresentation {
        return RidePresentation(
            ride.uid,
            user.name,
            user.email,
            "${user.carBrand} ${user.carModel}",
            user.carColor!!,
            user.profilePicture!!,
            ride.date,
            ride.time,
            ride.startLatitute,
            ride.startLongitude,
            ride.endLatitute,
            ride.endLongitude,
            ride.type,
            ride.places,
            ride.description,
            ride.acceptAlunos,
            ride.acceptDoc,
            ride.uniqueDrive,
            ride.price
        )
    }

    private fun menu(position: Int) {
        val rv = requireActivity().findViewById<RecyclerView>(R.id.rvMyActivePublications)

        val popUpMenu = PopupMenu(requireContext(), rv[position].findViewById(R.id.textViewOptions))
        popUpMenu.inflate(R.menu.my_active_rides_menu)

        popUpMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.edit -> {
                        return true
                    }
                }
                return false
            }
        })
        popUpMenu.show()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MyActiveRidesFragmentFragment().apply {

            }
    }
}
