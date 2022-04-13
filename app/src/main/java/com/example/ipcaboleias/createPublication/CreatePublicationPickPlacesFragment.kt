package com.example.ipcaboleias.createPublication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.NewPublicationAsPassenger
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationPickPlacesBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CreatePublicationPickPlacesFragment : Fragment(R.layout.fragment_create_publication_pick_places) {

    var _binding: FragmentCreatePublicationPickPlacesBinding? = null
    val binding get() = _binding!!

    private val CREATE_PUB_SEARCH1_FRAG_TAG = "createPubSearch1FragTag"
    private val CREATE_PUB_SEARCH2_FRAG_TAG = "createPubSearch2FragTag"
    private val CREATE_PUB_PICK_DATE_FRAG_TAG = "createPubPickDateFragTag"
    private val CREATE_PUB_PICK_TIME_FRAG_TAG = "createPubPickTimeFragTag"
    private val CREATE_PUB_PICK_PLACES_FRAG_TAG = "createPubPickPlacesFragTag"
    private val CREATE_PUB_PICK_PRICE_FRAG_TAG = "createPubPickPriceFragTag"

    private val model: NewPubViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationPickPlacesBinding.bind(view)

        binding.apply {
            btnLess.setOnClickListener {
                val places = tvNumber.text.toString().toInt()

                if (places > 1)
                    tvNumber.text = (places - 1).toString()
            }

            btnMore.setOnClickListener {
                val places = tvNumber.text.toString().toInt()

                if (places < 4)
                    tvNumber.text = (places + 1).toString()
            }

            passageiro.setOnClickListener {
                model.setType(NewPubViewModel.PubType.Passenger)
                createPublicationAsPassenger()
                closeAllFragments()
            }

            btnNext.setOnClickListener {
                model.setType(NewPubViewModel.PubType.Driver)
                model.setNPassengers(tvNumber.text.toString().toInt())

                val supportFragmentManager = requireActivity().supportFragmentManager

                val fragToHide = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PLACES_FRAG_TAG)
                val fragToCall = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PRICE_FRAG_TAG)

                if(fragToCall != null){
                    supportFragmentManager.beginTransaction().show(fragToCall).commit()
                }
                else{
                    supportFragmentManager.beginTransaction().add(
                        R.id.frameLayoutFilter,
                        CreatePublicationPickPriceFragment.newInstance(), CREATE_PUB_PICK_PRICE_FRAG_TAG).commit()
                }

                supportFragmentManager.beginTransaction().hide(fragToHide!!).commit()


                //Location.distanceBetween(model.getStartLatitude(), model.getStartLongitude(), model.getEndLatitude(), model.getEndLongitude(), results)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationPickPlacesFragment().apply {
            }
    }

    private fun closeAllFragments(){
        val supportFragmentManager = requireActivity().supportFragmentManager

        val fragCreatePubSearchStartPos = supportFragmentManager.findFragmentByTag(CREATE_PUB_SEARCH1_FRAG_TAG)
        val fragCreatePubSearchEndPos = supportFragmentManager.findFragmentByTag(CREATE_PUB_SEARCH2_FRAG_TAG)
        val fragCreatePubPickDate = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_DATE_FRAG_TAG)
        val fragCreatePubPickTime = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_TIME_FRAG_TAG)
        val fragCreatePubPickPlaces = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PLACES_FRAG_TAG)

        supportFragmentManager.beginTransaction().remove(fragCreatePubSearchStartPos!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubSearchEndPos!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickDate!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickTime!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickPlaces!!).commit()

    }

    private fun createPublicationAsPassenger() {
        var newPub = NewPublicationAsPassenger()
        newPub.startLatitute = model.getStartLatitude()
        newPub.startLongitude = model.getStartLongitude()
        newPub.endLatitute = model.getEndLatitude()
        newPub.endLongitude = model.getEndLongitude()
        newPub.date = model.getDate()
        newPub.time = model.getTime()
        newPub.type = model.getType().toString()


        val uid = requireActivity().intent.getStringExtra("uid")

        val database =
            Firebase.database("https://ipcaboleias-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef =
            database.getReference("publications/$uid${model.getType()}${model.getDate()}${model.getTime()}")

        myRef.setValue(newPub).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Publicação criada com sucesso!", Toast.LENGTH_LONG).show()
            }

        }.addOnFailureListener { exception ->
            Toast.makeText(activity, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

}


