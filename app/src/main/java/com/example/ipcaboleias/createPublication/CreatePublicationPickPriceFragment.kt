package com.example.ipcaboleias.createPublication

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.NewPublicationAsDriver
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationPickPriceBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CreatePublicationPickPriceFragment : Fragment(R.layout.fragment_create_publication_pick_price) {
    private var _binding: FragmentCreatePublicationPickPriceBinding? = null
    private val binding get() = _binding!!

    private val CREATE_PUB_SEARCH1_FRAG_TAG = "createPubSearch1FragTag"
    private val CREATE_PUB_SEARCH2_FRAG_TAG = "createPubSearch2FragTag"
    private val CREATE_PUB_PICK_DATE_FRAG_TAG = "createPubPickDateFragTag"
    private val CREATE_PUB_PICK_TIME_FRAG_TAG = "createPubPickTimeFragTag"
    private val CREATE_PUB_PICK_PLACES_FRAG_TAG = "createPubPickPlacesFragTag"
    private val CREATE_PUB_PICK_PRICE_FRAG_TAG = "createPubPickPriceFragTag"

    private val model: NewPubViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationPickPriceBinding.bind(view)


        binding.apply {

            val price = calculatePrice()

            tvPrice.setText("${price}€")

            viewConcordo.setOnClickListener {
                model.setPrice(price)
                createPublicationAsDriver()
                //Toast.makeText(requireContext(), "Publicação criada com sucesso.", Toast.LENGTH_LONG).show()
                closeFragments()
            }

            viewNConcordo.setOnClickListener {

            }
        }
    }

    private fun calculatePrice() : Float{
        var results = FloatArray(1)
        Location.distanceBetween(model.getStartLatitude(), model.getStartLongitude(), model.getEndLatitude(), model.getEndLongitude(), results)

        var distanceInKm : Float = results[0] / 1000
        // TODO: Perguntar a opinião de quanto cobrar por km
        // Vou utilizar 10 centimos por km só para testar

        return distanceInKm * 0.10f
    }

    private fun createPublicationAsDriver() {
        var newPub = NewPublicationAsDriver()
        newPub.startLatitute = model.getStartLatitude()
        newPub.startLongitude = model.getStartLongitude()
        newPub.endLatitute = model.getEndLatitude()
        newPub.endLongitude = model.getEndLongitude()
        newPub.date = model.getDate()
        newPub.time = model.getTime()
        newPub.type = model.getType().toString()
        newPub.price = model.getPrice()


        val uid = requireActivity().intent.getStringExtra("uid")

        val database =
            Firebase.database("https://ipcaboleias-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef =
            database.getReference("publications/$uid${model.getType()}${model.getDate()}${model.getTime()}")

        myRef.setValue(newPub).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Toast.makeText(activity, "Publicação criada com sucesso!", Toast.LENGTH_LONG).show()
            }

        }.addOnFailureListener { exception ->
            Toast.makeText(activity, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun closeFragments(){
        val supportFragmentManager = requireActivity().supportFragmentManager

        val fragCreatePubSearchStartPos = supportFragmentManager.findFragmentByTag(CREATE_PUB_SEARCH1_FRAG_TAG)
        val fragCreatePubSearchEndPos = supportFragmentManager.findFragmentByTag(CREATE_PUB_SEARCH2_FRAG_TAG)
        val fragCreatePubPickDate = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_DATE_FRAG_TAG)
        val fragCreatePubPickTime = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_TIME_FRAG_TAG)
        val fragCreatePubPickPlaces = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PLACES_FRAG_TAG)
        val fragCreatePubPickPrice = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PRICE_FRAG_TAG)

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