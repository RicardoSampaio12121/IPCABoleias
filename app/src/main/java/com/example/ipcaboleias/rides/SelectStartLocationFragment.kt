package com.example.ipcaboleias.rides

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.ViewModels.SelectStartLocationViewModel
import com.example.ipcaboleias.databinding.FragmentSelectStartLocationBinding
import com.google.android.gms.maps.model.LatLng

class SelectStartLocationFragment : Fragment(R.layout.fragment_select_start_location) {
    private var _binding: FragmentSelectStartLocationBinding? = null
    private val binding get() = _binding!!

    private val model: PublicationDetailsViewModel by activityViewModels()
    private val selectMode: SelectStartLocationViewModel by activityViewModels()

    private lateinit var adapter: RVSelectStartLocationAdapter
    private lateinit var ride: RidePresentation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ride = model.getRide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = com.example.ipcaboleias.databinding.FragmentSelectStartLocationBinding.bind(view)

        startAdapter()

        binding.apply {

        }

    }

    fun startAdapter() {
        val rvSLA = requireActivity().findViewById<RecyclerView>(R.id.rvSelectStartLocation)

        var l = com.google.android.gms.maps.model.LatLng(ride.startLatitude, ride.startLongitude)

        rvSLA.layoutManager = LinearLayoutManager(activity)

        adapter = RVSelectStartLocationAdapter(
            ride.stops,
            LatLng(ride.startLatitude, ride.startLongitude)
        )
        rvSLA.adapter = adapter

        adapter.setOnItemClickListener(object : RVSelectStartLocationAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                println("Position: $position")

                selectMode.startLatitude.value = ride.stops[position].latitude
                selectMode.startLongitude.value = ride.stops[position].longitude

                selectMode.clickedButton.value = !selectMode.clickedButton.value!!

                requireActivity().supportFragmentManager.beginTransaction()
                    .remove(this@SelectStartLocationFragment).commit()
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SelectStartLocationFragment().apply {
            }
    }
}