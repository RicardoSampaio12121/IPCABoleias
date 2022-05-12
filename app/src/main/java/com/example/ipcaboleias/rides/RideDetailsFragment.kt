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
import com.example.ipcaboleias.ChatActivity
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.databinding.FragmentRideDetailsBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.google.android.gms.location.GeofenceStatusCodes
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

class RideDetailsFragment : Fragment(R.layout.fragment_ride_details) {

    private var _binding: FragmentRideDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var usersRepo: UsersRepository
    private lateinit var pubRepo: PublicationsRepository

    private val model: PublicationDetailsViewModel by activityViewModels()
    private lateinit var ride: RidePresentation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ride = model.getRide()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        _binding = FragmentRideDetailsBinding.bind(view)

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

            val data = LocalDate.parse(
                ride.date,
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            )

            val d = Date.from(data.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
            val local = Locale("pt", "BR")
            val formato: DateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", local)

            txtRideInfoTitle.text = formato.format(d)


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
                pubRepo = PublicationsRepository(requireContext())
                pubRepo.reserveRide(ride.date, ride.time, ride.uid) {
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