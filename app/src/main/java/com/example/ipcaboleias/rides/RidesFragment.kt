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
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository

class RidesFragment : Fragment(R.layout.fragment_rides) {

    private val CREATE_PUB_SEARCH1_FRAG_TAG = "createPubSearch1FragTag"

    private var _binding : FragmentRidesBinding? = null
    private val binding get() = _binding!!

    val CREATE_PUB1_FRAG_TAG = "createPub1FragTag"
    val FILTER_FRAG_TAG = "filterFragTag"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRidesBinding.bind(view)

        binding.apply {

            btnAdd.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.beginTransaction().add(R.id.frameLayoutFilter, CreatePublicationSearchStartLocationFragment.newInstance(), CREATE_PUB_SEARCH1_FRAG_TAG).commit()
           }
        }

        val rv = requireView().findViewById<RecyclerView>(R.id.rvPublications)


        // Get rides from firestore

        val pubRepo = PublicationsRepository(requireContext())

        var adapter : RVPublicationsAadapter?

        pubRepo.getPublications(object : GetPublicationsCallback {
            override fun onCallback(rides: MutableList<Ride>) {
                adapter = RVPublicationsAadapter(rides)
                rv.adapter = adapter
                rv.layoutManager = LinearLayoutManager(activity)

                adapter!!.setOnItemClickListener(object : RVPublicationsAadapter.onItemClickListener {
                    override fun onItemClick(position: Int){
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.add(R.id.frameFragment, RideDetailsFragment.newInstance(), "detailsFragTag")?.commit()
                        transaction?.hide(activity?.supportFragmentManager?.findFragmentByTag("ridesFragTag")!!)
                        //transaction?.disallowAddToBackStack()
                    }
                })
            }
        })


        //var publicationList : MutableList<RVPublication>

//        var publicationList = mutableListOf(
//            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
//            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
//            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
//            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
//            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
//            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
//            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
//            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
//            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
//            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
//            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
//            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
//            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
//            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
//            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F)
//        )





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