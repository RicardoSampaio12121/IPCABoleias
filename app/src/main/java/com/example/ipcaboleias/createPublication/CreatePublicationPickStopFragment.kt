package com.example.ipcaboleias.createPublication

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationPickStopBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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


class CreatePublicationPickStopFragment : Fragment(R.layout.fragment_create_publication_pick_stop),
    OnMapReadyCallback {

    private val CREATE_PUB_ADD_STOP_FRAG_TAG = "createPubAddStopFragTag"

    private var _binding: FragmentCreatePublicationPickStopBinding? = null
    private val binding get() = _binding!!

    private var map: GoogleMap? = null

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val model: PickStopViewModel by activityViewModels()

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreatePublicationPickStopBinding.bind(view)

        val supportFragmentManager = requireActivity().supportFragmentManager

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.apply {
            buttonSearch.setOnClickListener {
                if (etSearchLocation.text.toString() == "") {
                    Toast.makeText(
                        requireContext(),
                        "Sem informações para procurar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                searchLocation(etSearchLocation.text.toString())
            }

            btnNext.setOnClickListener {
                model.latitude.value = latitude
                model.longitude.value = longitude
                model.buttonClicked.value = !model.buttonClicked.value!!

                requireActivity().supportFragmentManager.beginTransaction()
                    .remove(this@CreatePublicationPickStopFragment).commit()

                requireActivity().supportFragmentManager.beginTransaction().show(
                    supportFragmentManager.findFragmentByTag(CREATE_PUB_ADD_STOP_FRAG_TAG)!!
                ).commit()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationPickStopFragment().apply {
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMapReady(p0: GoogleMap) {
        map = p0

        // Define initial position of the map
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()

        map!!.setOnMapClickListener {
            latitude = it.latitude
            longitude = it.longitude

            map!!.clear()

            map!!.addMarker(
                MarkerOptions().position(it)
            )
            map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 16.0f))

            // Change location in editText
            //reverseGeocoding(it.latitude, it.longitude)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation
        val et = requireActivity().findViewById<EditText>(R.id.etSearchLocation)

        if (ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission
                    .ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest
                    .permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    android.Manifest.permission
                        .ACCESS_FINE_LOCATION
                ), 101
            )
            return
        }
        task.addOnSuccessListener {

            latitude = it.latitude
            longitude = it.longitude
            val coordinates = LatLng(it.latitude, it.longitude)

            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16.0f))
            map!!.addMarker(MarkerOptions().position(coordinates))
            //et.setText("${it.latitude} ${it.longitude}")
            reverseGeocoding(it.latitude, it.longitude)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun reverseGeocoding(lat: Double, long: Double) {
        GlobalScope.launch(Dispatchers.IO) {
            val url =
                URL("https://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$long&zoom=18")

            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"  // optional default is GET

                println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

                inputStream.bufferedReader().use {
                    val json = it.readText()
                    val topic = Gson().fromJson(json, ReverseGeo::class.java)

                    launch(Dispatchers.Main) {
                        val et = requireActivity().findViewById<EditText>(R.id.etSearchLocation)
                        et.setText(topic.address.county)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun searchLocation(location: String) {
        val geoCoder = Geocoder(activity)
        val address = geoCoder.getFromLocationName(location, 1)

        if (address.isEmpty()) {
            Toast.makeText(requireContext(), "Location not found.", Toast.LENGTH_SHORT).show()
            return
        }

        val coordinates = LatLng(address[0].latitude, address[0].longitude)

        map!!.clear()

        map!!.addMarker(MarkerOptions().position(coordinates))
        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16.0f))

        latitude = coordinates.latitude
        longitude = coordinates.longitude
        //reverseGeocoding(coordinates.latitude, coordinates.longitude)
    }
}