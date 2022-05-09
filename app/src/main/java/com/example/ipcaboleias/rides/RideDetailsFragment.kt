package com.example.ipcaboleias.rides

import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.ChatActivity
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.databinding.FragmentRideDetailsBinding
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.google.android.gms.location.GeofenceStatusCodes
import java.io.Serializable
import java.util.*
import java.util.regex.Pattern

class RideDetailsFragment : Fragment(R.layout.fragment_ride_details) {

    private var _binding: FragmentRideDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var usersRepo: UsersRepository

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

            val add = getLocation(ride.startLatitude, ride.startLongitude)

            // TODO: ARRUMAR ISTO NUMA FUNÇÃO
            tvFrom.text = add.getAddressLine(0)
            tvTo.text = ride.endLatitude.toString()
            tvPrice.text = "${ride.price}€"
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
        }

    }

    fun getLocation(latitude: Double, longitude: Double) : Address{
        val addresses: MutableList<Address>
        val geocoder = Geocoder(requireContext(), Locale.ENGLISH)

        addresses =  geocoder.getFromLocation(latitude, longitude, 1)

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