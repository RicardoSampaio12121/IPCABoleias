package com.example.ipcaboleias.rides

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.createPublication.CreatePublicationSearchStartLocationFragment
import com.example.ipcaboleias.databinding.FragmentRidesBinding
import com.example.ipcaboleias.firebaseRepository.Callbacks.GetPublicationsCallback
import com.example.ipcaboleias.firebaseRepository.Callbacks.TesteCallback
import com.example.ipcaboleias.firebaseRepository.Callbacks.UserCallback
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.example.ipcaboleias.registration.NewUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class RidesFragment : Fragment(R.layout.fragment_rides) {

    private val CREATE_PUB_SEARCH1_FRAG_TAG = "createPubSearch1FragTag"

    private var _binding: FragmentRidesBinding? = null
    private val binding get() = _binding!!
    private lateinit var publications: MutableList<RidePresentation>

    private lateinit var adapter: RVPublicationsAadapter
    private val model: PublicationDetailsViewModel by activityViewModels()

    val CREATE_PUB1_FRAG_TAG = "createPub1FragTag"
    val FILTER_FRAG_TAG = "filterFragTag"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRidesBinding.bind(view)
        val pubRepo = PublicationsRepository(requireContext())
        val usersRepo = UsersRepository(requireContext())

        publications = ArrayList()
        adapter = RVPublicationsAadapter(publications)

        binding.apply {

            // Evento click do botão para criar uma nova publicação
            btnAdd.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.beginTransaction().add(
                    R.id.frameLayoutFilter,
                    CreatePublicationSearchStartLocationFragment.newInstance(),
                    CREATE_PUB_SEARCH1_FRAG_TAG
                ).commit()
            }

            //Carregar recycler view com as publicações

            pubRepo.getPublications { it ->
                val size = it.size

                for (ride in it) {
                    usersRepo.getUser(ride.uid) { user ->
                        publications.add(
                            newRidePresentationObject(
                                ride,
                                user
                            )
                        )
                        adapter.notifyItemInserted(publications.size - 1)

                        updateRecyclerView()
                    }
                }
            }
        }
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

    private fun updateRecyclerView() {
        val rvPublications = requireActivity().findViewById<RecyclerView>(R.id.rvPublications)

        rvPublications.layoutManager = LinearLayoutManager(activity)
        adapter = RVPublicationsAadapter(publications)
        rvPublications.adapter = adapter

        adapter.setOnItemClickListener(object : RVPublicationsAadapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                model.setRide(publications[position])

                val transaction = activity?.supportFragmentManager?.beginTransaction()

                if (publications[position].type == "Passenger") {
                    transaction?.add(
                        R.id.frameFragment,
                        RideDetailsPassengerFragment.newInstance(),
                        "detailsPassengerFragTag"
                    )?.commit()
                } else {
                    transaction?.add(
                        R.id.frameFragment,
                        RideDetailsFragment.newInstance(),
                        "detailsFragTag"
                    )?.commit()

                }

            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RidesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}