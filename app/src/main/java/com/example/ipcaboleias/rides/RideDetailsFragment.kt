package com.example.ipcaboleias.rides

import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.ChatActivity
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.ViewModels.SelectStartLocationViewModel
import com.example.ipcaboleias.createPublication.NewStop
import com.example.ipcaboleias.databinding.FragmentRideDetailsBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.type.LatLng
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import java.util.regex.Pattern

class RideDetailsFragment : Fragment(R.layout.fragment_ride_details) {

    private val SELECT_START_LOCATION_FRAG_TAG = "selectStartLocationFragTag"
    private val POSSIBLE_STOP_MAP_VISUALIZER_FRAG_TAG = "possibleStopMapVisualizerFragTag"

    private var _binding: FragmentRideDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var usersRepo: UsersRepository
    private lateinit var pubRepo: PublicationsRepository

    private val model: PublicationDetailsViewModel by activityViewModels()
    private val selectMode: SelectStartLocationViewModel by activityViewModels()

    private lateinit var ride: RidePresentation

    private lateinit var adapter: RVPossibleStopsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ride = model.getRide()
        startSelectModel()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRideDetailsBinding.bind(view)

        startRVAdapter()

        var btnMenu = activity?.requireViewById<ImageButton>(R.id.imageMenu)
        var btnReturn = activity?.requireViewById<ImageButton>(R.id.imageReturn)

        btnMenu?.visibility = View.GONE
        btnReturn?.visibility = View.VISIBLE



        binding.apply {
            if (ride.stops.size == 0) {
                possibleStops.visibility = View.GONE
            }

            if (ride.description == "") {
                viewDescription.visibility = View.GONE
            }

            val location = getLocation(ride.startLatitude, ride.startLongitude)

            if (location == null) {
                tvFrom.text = "Ola mundo"
            } else {
                tvFrom.text = location.getAddressLine(0)
            }

            when (ride.endLatitude) {
                41.536587 -> {
                    tvTo.text = "IPCA Barcelos"
                }
                41.542142 -> {
                    tvTo.text = "IPCA Braga"
                }
                41.507823 -> {
                    tvTo.text = "IPCA Guimarães"
                }
                41.440063 -> {
                    tvTo.text = "IPCA Famalicão"
                }
            }

            val timestamp = ride.date
            val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
            val localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault())

            val localDate = localDateTime.toLocalDate()
            val localTime = localDateTime.toLocalTime()

            val ptLocale: Locale = Locale("pt", "PT")
            val month = localDate.month.getDisplayName(TextStyle.FULL, ptLocale)

            txtRideInfoTitle.text =
                "${localDate.dayOfMonth} de ${month} de ${localDate.year} às ${localTime.hour}:${localTime.minute} h"

            tvPrice.text = String.format("%.2f€", ride.price)
            tvPlaces.text = ride.places.toString()

            val byteArray: ByteArray = Base64.getDecoder().decode(ride.profilePicture)
            val bitMapPic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            profilePicture.setImageBitmap(bitMapPic)

            txtNamePerson.text = ride.name

            if (ride.acceptAlunos && ride.acceptDoc) tvDispCh.text = "Todos"
            else if (ride.acceptAlunos && !ride.acceptDoc) tvDispCh.text = "Alunos"
            else tvDispCh.text = "Docentes"


            if (Pattern.matches("a[0-9]+@alunos.ipca.pt", ride.email)) {
                tvDocOrStu.text = "Aluno do IPCA"
            } else {
                tvDocOrStu.text = "Docente do IPCA"
            }

            buttonContact.text = "CONTACTAR ${ride.name}"

            carBrand.text = ride.car
            tvColor.text = ride.carColor

            txtDesc.text = ride.description

            //--------------------------------------------------------------

            buttonContact.setOnClickListener {
                // Criar canal de contacto entre os dois utilizadores
                usersRepo = UsersRepository(requireContext())
                usersRepo.getOrCreateChatChannel(ride.uid) { channelId ->

                    // Mostrar ui de chat
                    val intent = Intent(requireActivity(), ChatActivity::class.java)
                    intent.putExtra("channelId", channelId)
                    requireActivity().startActivity(intent)
                }
            }

            buttonReserve.setOnClickListener {
                ride.stops.add(NewStop(ride.startLatitude, ride.startLongitude))

                if (ride.stops.size > 1) {
                    requireActivity().supportFragmentManager.beginTransaction().add(
                        R.id.frameFragment,
                        SelectStartLocationFragment.newInstance(),
                        SELECT_START_LOCATION_FRAG_TAG
                    ).commit()

                    selectLocationListener()
                } else {
                    pubRepo = PublicationsRepository(requireContext())
                    pubRepo.reserveRide(
                        ride.date,
                        ride.uid,
                        com.google.android.gms.maps.model.LatLng(
                            ride.startLatitude,
                            ride.startLongitude
                        )
                    ) {
                        if (it) {
                            Toast.makeText(
                                requireContext(),
                                "Reserva enviada para revisão.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    fun startSelectModel() {
        selectMode.clickedButton.value = false
    }

    fun selectLocationListener() {
        selectMode.clickedButton.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (selectMode.startLatitude.value == null) return@Observer

            pubRepo = PublicationsRepository(requireContext())
            pubRepo.reserveRide(
                ride.date,
                ride.uid,
                com.google.android.gms.maps.model.LatLng(
                    selectMode.startLatitude.value!!,
                    selectMode.startLongitude.value!!
                )
            ) {
                if (it) {
                    Toast.makeText(
                        requireContext(),
                        "Reserva enviada para revisão.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    fun startRVAdapter() {
        val rvStops = requireActivity().findViewById<RecyclerView>(R.id.rvPossibleStops)

        var l = com.google.android.gms.maps.model.LatLng(ride.startLatitude, ride.startLongitude)

        rvStops.layoutManager = LinearLayoutManager(activity)
        adapter = RVPossibleStopsAdapter(
            ride.stops,
            com.google.android.gms.maps.model.LatLng(ride.startLatitude, ride.startLongitude)
        )
        rvStops.adapter = adapter

        adapter.setOnItemClickListener(object : RVPossibleStopsAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.beginTransaction().add(
                    R.id.frameFragment,
                    PossibleStopMapVisualizerFragment.newInstance(
                        com.google.android.gms.maps.model.LatLng(
                            ride.stops[position].latitude,
                            ride.stops[position].longitude
                        )
                    ), POSSIBLE_STOP_MAP_VISUALIZER_FRAG_TAG
                ).commit()
            }
        })


    }

    fun getLocation(latitude: Double, longitude: Double): Address? {
        val addresses: MutableList<Address>
        val geocoder = Geocoder(requireContext(), Locale.ENGLISH)

        addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses.size == 0) {
            return null
        }

        return addresses[0]
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RideDetailsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}