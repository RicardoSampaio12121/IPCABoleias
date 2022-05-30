package com.example.ipcaboleias.createPublication

import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.NewPublicationAsDriver
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationPickPriceBinding
import com.example.ipcaboleias.firebaseRepository.Callbacks.NewPublicationCallback
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CreatePublicationPickPriceFragment :
    Fragment(R.layout.fragment_create_publication_pick_price) {
    private var _binding: FragmentCreatePublicationPickPriceBinding? = null
    private val binding get() = _binding!!

    private val CREATE_PUB_SEARCH1_FRAG_TAG = "createPubSearch1FragTag"
    private val CREATE_PUB_SEARCH2_FRAG_TAG = "createPubSearch2FragTag"
    private val CREATE_PUB_PICK_DATE_FRAG_TAG = "createPubPickDateFragTag"
    private val CREATE_PUB_PICK_TIME_FRAG_TAG = "createPubPickTimeFragTag"
    private val CREATE_PUB_PICK_PLACES_FRAG_TAG = "createPubPickPlacesFragTag"
    private val CREATE_PUB_PICK_PRICE_FRAG_TAG = "createPubPickPriceFragTag"
    private val CREATE_PUB_SET_CUSTOM_PRICE_FRAG_TAG = "createPubSetCustomPriceFragTag"

    private val model: NewPubViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationPickPriceBinding.bind(view)


        binding.apply {

            val price = calculatePrice()

            tvPrice.setText("${String.format("%.2f", price)}€")

            viewConcordo.setOnClickListener {
                val intent = requireActivity().intent
                model.setPrice(price)

                val newPub = NewPublicationAsDriver()
                newPub.uid = intent.getStringExtra("uid")!!
                newPub.startLatitute = model.getStartLatitude()
                newPub.startLongitude = model.getStartLongitude()
                newPub.endLatitute = model.getEndLatitude()
                newPub.endLongitude = model.getEndLongitude()
                newPub.date = model.getDate()
                newPub.time = model.getTime()
                newPub.type = model.getType().toString()
                newPub.places = model.getNPassengers()
                newPub.description = model.getDescription()
                newPub.uniqueDrive = model.getUniqueDrive()
                newPub.acceptDoc = model.getAcceptDoc()
                newPub.acceptAlunos = model.getAcceptAlunos()
                newPub.price = model.getPrice()!!
                newPub.stops = model.stops.value!!


                createPublicationAsDriver(newPub)
                Toast.makeText(activity, "Boleia adicionada com sucesso", Toast.LENGTH_LONG).show()

                closeFragments()
            }

            viewNConcordo.setOnClickListener{
                model.setPrice(price)

                //Abrir novo fragmento

                val supportFragmentManager = requireActivity().supportFragmentManager

                val fragToHide = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PRICE_FRAG_TAG)
                val fragToCall = supportFragmentManager.findFragmentByTag(CREATE_PUB_SET_CUSTOM_PRICE_FRAG_TAG)

                supportFragmentManager.beginTransaction().hide(fragToHide!!).commit()

                if(fragToCall != null){
                    supportFragmentManager.beginTransaction().show(fragToCall).commit()
                }
                else{
                    supportFragmentManager.beginTransaction().add(R.id.frameFragment, CreatePublicationSetCustomPriceFragment.newInstance(), CREATE_PUB_SET_CUSTOM_PRICE_FRAG_TAG).commit()
                }


            }
        }
    }

    private fun calculatePrice(): Float {
        var results = FloatArray(1)
        Location.distanceBetween(
            model.getStartLatitude(),
            model.getStartLongitude(),
            model.getEndLatitude(),
            model.getEndLongitude(),
            results
        )

        var distanceInKm: Float = results[0] / 1000

        return distanceInKm * 0.30f

        //return String.format("%.1f", (distanceInKm * 0.30f)).toFloat()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPublicationAsDriver(pub : NewPublicationAsDriver) {
        val repo = PublicationsRepository(requireContext())
        repo.createPublicationAsDriver(pub, object: NewPublicationCallback {
            override fun onCallback(success: Boolean) {
                if(success){
                    //Toast.makeText(activity, "ola valete", Toast.LENGTH_SHORT).show()
                }else{
                    //Toast.makeText(activity, "oi menu nome é vanessa", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun closeFragments() {
        val supportFragmentManager = requireActivity().supportFragmentManager

        val fragCreatePubSearchStartPos =
            supportFragmentManager.findFragmentByTag(CREATE_PUB_SEARCH1_FRAG_TAG)
        val fragCreatePubSearchEndPos =
            supportFragmentManager.findFragmentByTag(CREATE_PUB_SEARCH2_FRAG_TAG)
        val fragCreatePubPickDate =
            supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_DATE_FRAG_TAG)
        val fragCreatePubPickTime =
            supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_TIME_FRAG_TAG)
        val fragCreatePubPickPlaces =
            supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PLACES_FRAG_TAG)
        val fragCreatePubPickPrice =
            supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PRICE_FRAG_TAG)

        supportFragmentManager.beginTransaction().remove(fragCreatePubSearchStartPos!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubSearchEndPos!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickDate!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickTime!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickPlaces!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickPrice!!).commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationPickPriceFragment().apply {
            }
    }
}