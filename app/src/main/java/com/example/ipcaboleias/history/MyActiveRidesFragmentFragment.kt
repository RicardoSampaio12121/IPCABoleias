package com.example.ipcaboleias.history

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.history.PassengersFragment
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.databinding.FragmentMyActiveRidesFragmentBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.RidesWithDocId
import com.example.ipcaboleias.firebaseRepository.User
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.example.ipcaboleias.registration.NewUser
import com.example.ipcaboleias.rides.RideDetailsFragment
import com.example.ipcaboleias.rides.RideDetailsPassengerFragment
import com.example.ipcaboleias.rides.RidePresentation


class MyActiveRidesFragmentFragment : Fragment(R.layout.fragment_my_active_rides_fragment) {
    private var _binding: FragmentMyActiveRidesFragmentBinding? = null
    private val binding get() = _binding!!

    private val RIDES_DETAILS_FRAG_TAG = "detailsFragTag"
    private val RIDES_DETAILS_PASSENGER_FRAG_TAG = "detailsPassengerFragTag"

    private val model: PublicationDetailsViewModel by activityViewModels()

    private lateinit var myRides: MutableList<RidesWithDocId>
    private lateinit var myRidesIds: MutableList<String>

    private lateinit var pubRepo: PublicationsRepository
    private lateinit var usersRepo: UsersRepository
    private lateinit var adapter: RVmyActiveRidesAdapter

    private val EDIT_PUB_FRAG_TAG = "editPubFragTag"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMyActiveRidesFragmentBinding.bind(view)
        pubRepo = PublicationsRepository(requireContext())
        usersRepo = UsersRepository(requireContext())
        myRides = ArrayList()
        myRidesIds = ArrayList()

//        adapter = RVmyActiveRidesAdapter(
//            myRides,
//            object : RVmyActiveRidesAdapter.OptionsMenuClickListener {
//                override fun onOptionsMenuClicked(position: Int) {
//                    menu(position)
//                }
//            })


        binding.apply {
            usersRepo.getCurrentUserActivePublicationsAsDriverIds {
                val size = it.size
                var iterator = 0

                for (id in it) {
                    pubRepo.getPublicationByIdWithDocId(id.docId) { ride ->
                        iterator++
                        myRides.add(ride)

                        if (iterator + 1 > size) {
                            myRides.sortBy { it.dateTime }
                            updateRecyclerView()
                        }
                    }
                }
            }
        }
    }

    private fun updateRecyclerView() {
        val rvMyActiveRides =
            requireActivity().findViewById<RecyclerView>(R.id.rvMyActivePublications)

        val supportFragmentManager = requireActivity().supportFragmentManager

        rvMyActiveRides.layoutManager = LinearLayoutManager(activity)

        adapter = RVmyActiveRidesAdapter(
            myRides,
            object : RVmyActiveRidesAdapter.OptionsMenuClickListener {
                override fun onOptionsMenuClicked(position: Int) {
                    menu(position)
                }
            },
            object : RVmyActiveRidesAdapter.SeePassengersClickListener {
                override fun onSeePassengersClickListener(position: Int) {

                    supportFragmentManager.beginTransaction().add(
                        R.id.frameFragment,
                        PassengersFragment.newInstance(
                            myRides[position].docId,
                            myRides[position].endLatitute.toString(),
                            myRides[position].endLongitude.toString()
                        )
                    ).commit()
                }
            })

        rvMyActiveRides.adapter = adapter

        adapter.setOnItemClickListener(object : RVmyActiveRidesAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val supportFragmentManager = requireActivity().supportFragmentManager

                usersRepo = UsersRepository(requireContext())
                usersRepo.getCurrentUser {
                    var ride = newRidePresentationObject(myRides[position], it)
                    model.setRide(ride)

                    if (ride.type == "Passenger") {
                        supportFragmentManager.beginTransaction().add(
                            R.id.frameFragment,
                            RideDetailsPassengerFragment.newInstance(),
                            RIDES_DETAILS_PASSENGER_FRAG_TAG
                        ).commit()
                    } else {
                        supportFragmentManager.beginTransaction().add(
                            R.id.frameFragment,
                            RideDetailsFragment.newInstance(),
                            RIDES_DETAILS_FRAG_TAG
                        ).commit()
                    }


                }
            }
        })
    }

    private fun newRidePresentationObject(ride: RidesWithDocId, user: User): RidePresentation {
        return RidePresentation(
            ride.uid,
            user.name,
            user.email,
            "${user.carBrand} ${user.carModel}",
            user.carColor!!,
            user.profilePicture!!,
            ride.dateTime,
            ride.startLatitute,
            ride.startLongitude,
            ride.endLatitute,
            ride.endLongitude,
            ride.type,
            ride.places,
            ride.description,
            ride.acceptAlunos,
            ride.acceptDoc,
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
                        val supportFragmentManager = requireActivity().supportFragmentManager

                        usersRepo.getCurrentUser {
                            val ride = newRidePresentationObject(myRides[position], it)
                            model.setRide(ride)

                            supportFragmentManager.beginTransaction().add(
                                R.id.frameFragment,
                                EditPublicationFragment.newInstance(myRides[position].docId),
                                EDIT_PUB_FRAG_TAG
                            ).commit()
                        }
                    }
                    R.id.deactivate -> {
                        pubRepo.deactivateRide(myRides[position].docId) {
                            adapter.removeItem(position)
                        }
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
