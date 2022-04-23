package com.example.ipcaboleias.rides

import android.graphics.BitmapFactory
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
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.databinding.FragmentRideDetailsBinding
import java.io.Serializable
import java.util.*

class RideDetailsFragment : Fragment(R.layout.fragment_ride_details) {

    private var _binding: FragmentRideDetailsBinding? = null
    private val binding get() = _binding!!

    private val model: PublicationDetailsViewModel by activityViewModels()
    private lateinit var ride : Ride

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
            tvFrom.text = ride.startLatitude.toString()
            tvTo.text = ride.endLatitude.toString()
            timeFrom.text = ride.time
            timeTo.text = ride.time
            tvPrice.text = "${ride.price}€"
            tvPlaces.text = ride.places.toString()

            val byteArray : ByteArray = Base64.getDecoder().decode(ride.profilePicture)
            val bitMapPic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            profilePicture.setImageBitmap(bitMapPic)

            txtNamePerson.text = ride.name
            //TODO: VER SE É DOCENTE OU ALUNO DO IPCA
            buttonContact.text = "CONTACTAR ${ride.name}"
            //TODO: SÓ TENHO A MARCA DO CARRO, FALTA O RESTO
            carBrand.text = ride.car

            txtDesc.text = ride.description
        }

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