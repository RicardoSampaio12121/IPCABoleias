package com.example.ipcaboleias.createPublication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationPickPlacesBinding


class CreatePublicationPickPlacesFragment :
    Fragment(R.layout.fragment_create_publication_pick_places) {

    var _binding: FragmentCreatePublicationPickPlacesBinding? = null
    val binding get() = _binding!!

    private val CREATE_PUB_SEARCH1_FRAG_TAG = "createPubSearch1FragTag"
    private val CREATE_PUB_SEARCH2_FRAG_TAG = "createPubSearch2FragTag"
    private val CREATE_PUB_PICK_DATE_FRAG_TAG = "createPubPickDateFragTag"
    private val CREATE_PUB_PICK_TIME_FRAG_TAG = "createPubPickTimeFragTag"
    private val CREATE_PUB_PICK_PLACES_FRAG_TAG = "createPubPickPlacesFragTag"
    private val CREATE_PUB_PICK_PRICE_FRAG_TAG = "createPubPickPriceFragTag"
    private val CREATE_PUB_ADD_DESCRIPTION_FRAG_TAG = "createPubAddDescriptionFragTag"
    private val CREATE_PUB_SET_DEFINITIONS_PASSENGER_FRAG_TAG =
        "createPubSetDefinitionsPassengerFragTag"

    private val model: NewPubViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationPickPlacesBinding.bind(view)
        val supportFragmentManager = requireActivity().supportFragmentManager

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

                val fragToHide =
                    supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PLACES_FRAG_TAG)

                supportFragmentManager.beginTransaction().add(
                    R.id.frameFragment,
                    CreatePublicationSetDefinitionsPassengerFragment.newInstance(),
                    CREATE_PUB_SET_DEFINITIONS_PASSENGER_FRAG_TAG
                ).commit()

                supportFragmentManager.beginTransaction().hide(fragToHide!!).commit()

            }

            btnNext.setOnClickListener {
                model.setType(NewPubViewModel.PubType.Driver)
                model.setNPassengers(tvNumber.text.toString().toInt())

                val fragToHide =
                    supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PLACES_FRAG_TAG)
                val fragToCall =
                    supportFragmentManager.findFragmentByTag(CREATE_PUB_ADD_DESCRIPTION_FRAG_TAG)

                if (fragToCall != null) {
                    supportFragmentManager.beginTransaction().show(fragToCall).commit()
                } else {
                    supportFragmentManager.beginTransaction().add(
                        R.id.frameLayoutFilter,
                        CreatePublicationAddDescriptionFragment.newInstance(),
                        CREATE_PUB_ADD_DESCRIPTION_FRAG_TAG
                    ).commit()
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
}


