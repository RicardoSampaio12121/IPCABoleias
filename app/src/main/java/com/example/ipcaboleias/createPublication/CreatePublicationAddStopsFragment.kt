package com.example.ipcaboleias.createPublication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationAddStopsBinding
import com.example.ipcaboleias.rides.RVPublicationsAadapter
import com.google.android.gms.maps.SupportMapFragment

class CreatePublicationAddStopsFragment : Fragment(R.layout.fragment_create_publication_add_stops) {

    private val CREATE_PUB_PICK_STOP = "createPubPickStop"
    private val CREATE_PUB_PICK_DATE_FRAG_TAG = "createPubPickDateFragTag"
    private val CREATE_PUB_ADD_DESCRIPTION_FRAG_TAG = "createPubAddDescriptionFragTag"

    private var _binding: FragmentCreatePublicationAddStopsBinding? = null
    private val binding get() = _binding!!

    private val stops = ArrayList<NewStop>()

    private lateinit var adapter: RVAddStopAdapter


    private val model: PickStopViewModel by activityViewModels()
    private val NPmodel: NewPubViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreatePublicationAddStopsBinding.bind(view)

        startViewModel()
        startAdapter()
        startListener()

        binding.apply {
            btnAddStop.setOnClickListener {
                // Abrir fragmento com mapa para escolher local

                requireActivity().supportFragmentManager.beginTransaction().add(
                    R.id.frameFragment,
                    CreatePublicationPickStopFragment.newInstance(),
                    CREATE_PUB_PICK_STOP
                ).commit()

                requireActivity().supportFragmentManager.beginTransaction()
                    .hide(this@CreatePublicationAddStopsFragment).commit()
            }

            btnNext.setOnClickListener {
                NPmodel.stops.value = stops

                requireActivity().supportFragmentManager.beginTransaction().add(
                    R.id.frameLayoutFilter,
                    CreatePublicationAddDescriptionFragment.newInstance(),
                    CREATE_PUB_ADD_DESCRIPTION_FRAG_TAG
                ).commit()

                requireActivity().supportFragmentManager.beginTransaction()
                    .hide(this@CreatePublicationAddStopsFragment).commit()
            }

        }
    }

    private fun startViewModel() {
        model.buttonClicked.value = false
    }

    fun startListener() {
        model.buttonClicked.observe(viewLifecycleOwner, Observer {
            if (model.latitude.value != null) {
                stops.add(NewStop(model.latitude.value!!, model.longitude.value!!))
                adapter.notifyItemInserted(stops.size - 1)
            }
        })
    }

    fun startAdapter() {
        val rvStops = requireActivity().findViewById<RecyclerView>(R.id.recyclerView)

        rvStops.layoutManager = LinearLayoutManager(activity)
        adapter = RVAddStopAdapter(stops)
        rvStops.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationAddStopsFragment().apply {
            }
    }
}