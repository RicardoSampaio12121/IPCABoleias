package com.example.ipcaboleias.rides

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.R
import com.example.ipcaboleias.createPublication.CreatePublicationSearchStartLocationFragment
import com.example.ipcaboleias.databinding.FragmentRidesBinding
import com.example.ipcaboleias.firebaseRepository.Callbacks.GetPublicationsCallback
import com.example.ipcaboleias.firebaseRepository.Callbacks.TesteCallback
import com.example.ipcaboleias.firebaseRepository.Callbacks.UserCallback
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.registration.NewUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class RidesFragment : Fragment(R.layout.fragment_rides) {

    private val CREATE_PUB_SEARCH1_FRAG_TAG = "createPubSearch1FragTag"

    private var _binding: FragmentRidesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RVPublicationsAadapter

    val CREATE_PUB1_FRAG_TAG = "createPub1FragTag"
    val FILTER_FRAG_TAG = "filterFragTag"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRidesBinding.bind(view)
        val pubRepo = PublicationsRepository(requireContext())

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

            // Vai buscar as publicações ao firestore
            pubRepo.getPublications(object : GetPublicationsCallback {
                override fun onCallback(rides: MutableList<Ride>) {
                    adapter = RVPublicationsAadapter(rides)
                    rvPublications.adapter = adapter
                    rvPublications.layoutManager = LinearLayoutManager(activity)

                    adapter.setOnItemClickListener(object :
                        RVPublicationsAadapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
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

    companion object {
        @JvmStatic
        fun newInstance() =
            RidesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}