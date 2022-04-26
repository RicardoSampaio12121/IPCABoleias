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
    private lateinit var publications: MutableList<Ride>

    private lateinit var adapter: RVPublicationsAadapter
    private val model: PublicationDetailsViewModel by activityViewModels()

    val CREATE_PUB1_FRAG_TAG = "createPub1FragTag"
    val FILTER_FRAG_TAG = "filterFragTag"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRidesBinding.bind(view)
        val pubRepo = PublicationsRepository(requireContext())
        val usersRepo = UsersRepository(requireContext())

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

            runBlocking {
                // Vai buscar as publicações ao firestore

                lifecycleScope.launch {
                    withContext(Dispatchers.Default) {
                        pubRepo.getPublications(object : GetPublicationsCallback {
                            override fun onCallback(rides: MutableList<Ride>) {
                                publications = rides

                                for (ride in rides) {
                                    // Buscar o utilizador de cada ride
                                    usersRepo.getUser(ride.uid, object : UserCallback {
                                        override fun onCallback(user: NewUser) {
                                            ride.name = user.name
                                            ride.email = user.email
                                            ride.profilePicture = user.profilePicture!!
                                            ride.car = "${user.carBrand} ${user.carModel}"
                                            ride.carColor = user.carColor!!
                                        }
                                    })
                                }
                                publications = rides
                                adapter = RVPublicationsAadapter(publications)
                                rvPublications.adapter = adapter
                                rvPublications.layoutManager = LinearLayoutManager(activity)

                                adapter.setOnItemClickListener(object :
                                RVPublicationsAadapter.onItemClickListener{
                                    override fun onItemClick(position : Int) {
                                        model.setRide(publications[position])

                                        val transaction = activity?.supportFragmentManager?.beginTransaction()

                                        transaction?.add(
                                            R.id.frameFragment,
                                            RideDetailsFragment.newInstance(),
                                            "detailsFragTag"
                                        )?.commit()
                                        transaction?.hide(activity?.supportFragmentManager?.findFragmentByTag("ridesFragTag")!!)
                                    }
                                })
                            }
                        })
                    }
                }

            }
        }
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