package com.example.ipcaboleias.createPublication

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.createPublication.ReverseGeo
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationSearchEndLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class CreatePublicationSearchEndLocationFragment :
    Fragment(R.layout.fragment_create_publication_search_end_location), OnMapReadyCallback {

    private var _binding: FragmentCreatePublicationSearchEndLocationBinding? = null
    private val binding get() = _binding!!

    private val CREATE_PUB_SEARCH2_FRAG_TAG = "createPubSearch2FragTag"
    private val CREATE_PUB_PICK_DATE_FRAG_TAG = "createPubPickDateFragTag"
    private val CREATE_PUB_ADD_STOP_FRAG_TAG = "createPubAddStopFragTag"

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var map: GoogleMap? = null

    private val model: NewPubViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationSearchEndLocationBinding.bind(view)


        val supportFragmentManager = requireActivity().supportFragmentManager
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val spinner = requireActivity().findViewById<Spinner>(R.id.spinnerSearchLocation)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.IPCAPolos,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        mapFragment.getMapAsync(this)

        binding.apply {


            spinnerSearchLocation.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                        lateinit var latLng: LatLng

                        when (p2) {
                            0 -> latLng = LatLng(41.536587, -8.627911)
                            1 -> latLng = LatLng(41.542142, -8.421324)
                            2 -> latLng = LatLng(41.507823, -8.335278)
                            3 -> latLng = LatLng(41.440063, -8.477200)
                        }
                        changeMapLocationLatLng(latLng)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }


            btnNext.setOnClickListener {
                model.setEndLatitude(latitude)
                model.setEndLongitude(longitude)

                val fragToHide =
                    supportFragmentManager.findFragmentByTag(CREATE_PUB_SEARCH2_FRAG_TAG)
                val fragToCall =
                    supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_DATE_FRAG_TAG)

                if (fragToCall != null) {
                    supportFragmentManager.beginTransaction().show(fragToCall).commit()
                } else {
                    supportFragmentManager.beginTransaction().add(
                        R.id.frameLayoutFilter,
                        CreatePublicationPickDateFragment.newInstance(),
                        CREATE_PUB_PICK_DATE_FRAG_TAG
                    ).commit()
                }

                supportFragmentManager.beginTransaction().hide(fragToHide!!).commit()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationSearchEndLocationFragment().apply {
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMapReady(p0: GoogleMap) {
        map = p0

        val latLng: LatLng = LatLng(41.536587, -8.627911)

        map!!.addMarker(MarkerOptions().position(latLng))
        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))

    }

    private fun changeMapLocationLatLng(latLng: LatLng) {
        map!!.clear()

        latitude = latLng.latitude
        longitude = latLng.longitude

        map!!.addMarker(MarkerOptions().position(latLng))
        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))
    }

}
