package com.example.ipcaboleias.createPublication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.NewPublicationAsDriver
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationSetCustomPriceBinding
import com.example.ipcaboleias.firebaseRepository.Callbacks.NewPublicationCallback
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository

class CreatePublicationSetCustomPriceFragment :
    Fragment(R.layout.fragment_create_publication_set_custom_price) {

    private val CREATE_PUB_SEARCH1_FRAG_TAG = "createPubSearch1FragTag"
    private val CREATE_PUB_SEARCH2_FRAG_TAG = "createPubSearch2FragTag"
    private val CREATE_PUB_PICK_DATE_FRAG_TAG = "createPubPickDateFragTag"
    private val CREATE_PUB_PICK_TIME_FRAG_TAG = "createPubPickTimeFragTag"
    private val CREATE_PUB_PICK_PLACES_FRAG_TAG = "createPubPickPlacesFragTag"
    private val CREATE_PUB_PICK_PRICE_FRAG_TAG = "createPubPickPriceFragTag"
    private val CREATE_PUB_SET_CUSTOM_PRICE_FRAG_TAG = "createPubSetCustomPriceFragTag"

    private var _binding: FragmentCreatePublicationSetCustomPriceBinding? = null
    private val binding get() = _binding!!

    private val model: NewPubViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationSetCustomPriceBinding.bind(view)

        binding.apply {
            val maxPrice = model.getPrice()!! + 3
            val minPrice = model.getPrice()!! - 3

            tvNumber.text = String.format("%.2f", model.getPrice())

            btnMore.setOnClickListener {
                if (model.getPrice()!! + 1 <= maxPrice) {
                    model.setPrice(model.getPrice()!! + 1)
                    tvNumber.text = String.format("%.2f", model.getPrice())
                }
            }
            btnLess.setOnClickListener {
                if (model.getPrice()!! - 1 >= minPrice) {
                    model.setPrice(model.getPrice()!! - 1)
                    tvNumber.text = String.format("%.2f", model.getPrice())
                }
            }

            btnNext.setOnClickListener {
                val intent = requireActivity().intent

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

                createPublicationAsDriver(newPub)
                Toast.makeText(activity, "Boleia adicionada com sucesso", Toast.LENGTH_LONG).show()

                closeFragments()

            }

        }

    }

    private fun createPublicationAsDriver(pub: NewPublicationAsDriver) {

        val repo = PublicationsRepository(requireContext())
        repo.createPublicationAsDriver(pub, object : NewPublicationCallback {
            override fun onCallback(success: Boolean) {
                if (success) {
                    //Toast.makeText(activity, "ola valete", Toast.LENGTH_SHORT).show()
                } else {
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
        val fragCreatePubSetCustomPrice =
            supportFragmentManager.findFragmentByTag(CREATE_PUB_SET_CUSTOM_PRICE_FRAG_TAG)

        supportFragmentManager.beginTransaction().remove(fragCreatePubSearchStartPos!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubSearchEndPos!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickDate!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickTime!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickPlaces!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickPrice!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubSetCustomPrice!!).commit()

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationSetCustomPriceFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}