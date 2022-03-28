package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RidesFragment : Fragment() {

    val CREATE_PUB_FRAG_TAG = "createPubFragTag"
    val FILTER_FRAG_TAG = "filterFragTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rides, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = requireView().findViewById<RecyclerView>(R.id.rvPublications)

        var publicationList = mutableListOf(
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F)
        )

        val adapter = RVPublicationsAadapter(publicationList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(activity)
        adapter.setOnItemClickListener(object : RVPublicationsAadapter.onItemClickListener{
            override fun onItemClick(position: Int){
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.add(R.id.frameFragment, RideDetailsFragment.newInstance(), "detailsFragTag")?.commit()
                transaction?.hide(activity?.supportFragmentManager?.findFragmentByTag("ridesFragTag")!!)
                //transaction?.disallowAddToBackStack()
            }
        })

        val createPubBtn = requireView().findViewById<ImageButton>(R.id.btnAdd)
        createPubBtn.setOnClickListener{
            val supportFragmentManager = requireActivity().supportFragmentManager
            val filterFrag = supportFragmentManager.findFragmentByTag(FILTER_FRAG_TAG)

            if(filterFrag != null && filterFrag.isVisible){
                supportFragmentManager.beginTransaction().hide(filterFrag)
            }
            supportFragmentManager.beginTransaction().add(R.id.frameLayoutFilter, CreatePublicationFragment.newInstance(), CREATE_PUB_FRAG_TAG).commit()
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