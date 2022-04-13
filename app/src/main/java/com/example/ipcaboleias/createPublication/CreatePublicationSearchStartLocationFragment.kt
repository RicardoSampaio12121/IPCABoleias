package com.example.ipcaboleias.createPublication

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.createPublication.ReverseGeo
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationSearchStartLocationBinding
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

class CreatePublicationSearchStartLocationFragment :
    Fragment(R.layout.fragment_create_publication_search_start_location), OnMapReadyCallback {

    private val CREATE_PUB_SEARCH1_FRAG_TAG = "createPubSearch1FragTag"
    private val CREATE_PUB_SEARCH2_FRAG_TAG = "createPubSearch2FragTag"


    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var _binding: FragmentCreatePublicationSearchStartLocationBinding? = null
    private val binding get() = _binding!!

    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    private var map: GoogleMap? = null

    private val model: NewPubViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationSearchStartLocationBinding.bind(view)


        val supportFragmentManager = requireActivity().supportFragmentManager
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        binding.apply {
            buttonSearch.setOnClickListener {
                if(etSearchLocation.text.toString() == ""){
                    Toast.makeText(requireContext(), "Sem informações para procurar", Toast.LENGTH_SHORT).show()
                }
                searchLocation(etSearchLocation.text.toString())
            }

            btnNext.setOnClickListener {
                // Guardar dados
                model.setStartLatitude(latitude)
                model.setStartLongitude(longitude)

                val fragToCall = supportFragmentManager.findFragmentByTag(CREATE_PUB_SEARCH2_FRAG_TAG)
                val fragToHide = supportFragmentManager.findFragmentByTag(CREATE_PUB_SEARCH1_FRAG_TAG)

                if(fragToCall != null){
                    supportFragmentManager.beginTransaction().show(fragToCall).commit()
                }else{
                    supportFragmentManager.beginTransaction().add(
                        R.id.frameLayoutFilter,
                        CreatePublicationSearchEndLocationFragment.newInstance(), CREATE_PUB_SEARCH2_FRAG_TAG).commit()
                }
                supportFragmentManager.beginTransaction().hide(fragToHide!!).commit()
            }
        }
    }

    companion object {
        fun newInstance() =
            CreatePublicationSearchStartLocationFragment().apply {

            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMapReady(p0: GoogleMap) {
        map = p0

        // Define initial position of the map
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
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
            reverseGeocoding(it.latitude, it.longitude)
        }
    }



    @RequiresApi(Build.VERSION_CODES.N)
    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation
        val et = requireActivity().findViewById<EditText>(R.id.etSearchLocation)

        if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest
                .permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission
                .ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {

            latitude = it.latitude
            longitude = it.longitude
            val coordinates = LatLng(it.latitude, it.longitude)

            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16.0f))
            //et.setText("${it.latitude} ${it.longitude}")
            reverseGeocoding(it.latitude, it.longitude)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun reverseGeocoding(lat : Double, long : Double){
        GlobalScope.launch(Dispatchers.IO) {
            val url =  URL("https://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$long&zoom=18")

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
    private fun searchLocation(location : String){
        val geoCoder = Geocoder(activity)
        val address = geoCoder.getFromLocationName(location, 1)

        val coordinates = LatLng(address[0].latitude, address[0].longitude)

        map!!.clear()

        map!!.addMarker(MarkerOptions().position(coordinates))
        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16.0f))

        latitude = coordinates.latitude
        longitude = coordinates.longitude
        reverseGeocoding(coordinates.latitude, coordinates.longitude)
    }
}
