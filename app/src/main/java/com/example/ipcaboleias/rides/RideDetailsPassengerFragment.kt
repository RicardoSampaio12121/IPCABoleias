package com.example.ipcaboleias.rides

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.ChatActivity
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.databinding.FragmentRideDetailsPassengerBinding
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

class RideDetailsPassengerFragment : Fragment(R.layout.fragment_ride_details_passenger) {
    private var _binding: FragmentRideDetailsPassengerBinding? = null
    private val binding get() = _binding!!

    private val model: PublicationDetailsViewModel by activityViewModels()
    private lateinit var ride: RidePresentation
    private lateinit var usersRepo: UsersRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ride = model.getRide()

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRideDetailsPassengerBinding.bind(view)

        var btnMenu = activity?.requireViewById<ImageButton>(R.id.imageMenu)
        var btnReturn = activity?.requireViewById<ImageButton>(R.id.imageReturn)

        btnMenu?.visibility = View.GONE
        btnReturn?.visibility = View.VISIBLE

        binding.apply {

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


            val d = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
            val local = Locale("pt", "BR")
            val formato: DateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", local)

            txtRideInfoTitle.text = formato.format(d)

            if (ride.acceptAlunos && ride.acceptDoc) tvDispCh.text = "Todos"
            else if (ride.acceptAlunos && !ride.acceptDoc) tvDispCh.text = "Alunos"
            else tvDispCh.text = "Docentes"

            if (Pattern.matches("a[0-9]+@alunos.ipca.pt", ride.email)) {
                tvDocOrStu.text = "Aluno do IPCA"
            } else {
                tvDocOrStu.text = "Docente do IPCA"
            }

            buttonContact.text = "CONTACTAR ${ride.name}"

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

        }

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
            RideDetailsPassengerFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}