package com.example.ipcaboleias.rides

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.example.ipcaboleias.R
import com.example.ipcaboleias.databinding.FragmentCreatePublicationSearchStartLocationBinding
import com.example.ipcaboleias.databinding.FragmentPossibleStopMapVisualizerBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private const val LATITUDE = "param1"
private const val LONGITUDE = "param2"


class PossibleStopMapVisualizerFragment : Fragment(R.layout.fragment_possible_stop_map_visualizer),
    OnMapReadyCallback {
    private var _binding: FragmentPossibleStopMapVisualizerBinding? = null
    private val binding get() = _binding!!

    private var latitude: Double? = null
    private var longitude: Double? = null

    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble(LATITUDE)
            longitude = it.getDouble(LONGITUDE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPossibleStopMapVisualizerBinding.bind(view)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val btnMenu = activity?.requireViewById<ImageButton>(R.id.imageMenu)
        val btnReturn = activity?.requireViewById<ImageButton>(R.id.imageReturn)

        btnMenu?.visibility = View.GONE
        btnReturn?.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(coordinates: LatLng) =
            PossibleStopMapVisualizerFragment().apply {
                arguments = Bundle().apply {
                    putDouble(LATITUDE, coordinates.latitude)
                    putDouble(LONGITUDE, coordinates.longitude)
                }
            }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map!!.addMarker(MarkerOptions().position(LatLng(latitude!!, longitude!!)))
        map!!.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude!!, longitude!!),
                16.0f
            )
        )
    }
}