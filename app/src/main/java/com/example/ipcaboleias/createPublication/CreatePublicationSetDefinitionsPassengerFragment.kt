package com.example.ipcaboleias.createPublication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.NewPublicationAsPassenger
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationSetDefinitionsPassengerBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.google.firebase.auth.FirebaseAuth

class CreatePublicationSetDefinitionsPassengerFragment :
    Fragment(R.layout.fragment_create_publication_set_definitions_passenger) {
    private var _binding: FragmentCreatePublicationSetDefinitionsPassengerBinding? = null
    private val binding get() = _binding!!
    private val model: NewPubViewModel by activityViewModels()

    private val CREATE_PUB_SEARCH1_FRAG_TAG = "createPubSearch1FragTag"
    private val CREATE_PUB_SEARCH2_FRAG_TAG = "createPubSearch2FragTag"
    private val CREATE_PUB_PICK_DATE_FRAG_TAG = "createPubPickDateFragTag"
    private val CREATE_PUB_PICK_TIME_FRAG_TAG = "createPubPickTimeFragTag"
    private val CREATE_PUB_PICK_PLACES_FRAG_TAG = "createPubPickPlacesFragTag"

    private lateinit var pubRepo: PublicationsRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreatePublicationSetDefinitionsPassengerBinding.bind(view)

        binding.apply {
            stDoc.setOnClickListener {
                if (!stDoc.isChecked && !stAlunos.isChecked) {
                    stAlunos.isChecked = true
                }
            }

            stAlunos.setOnClickListener {
                if (!stDoc.isChecked && !stAlunos.isChecked) {
                    stDoc.isChecked = true
                }
            }

            btnNext.setOnClickListener {
                model.setAcceptAlunos(stAlunos.isChecked)
                model.setAcceptDoc(stDoc.isChecked)

                createPublicationAsPassenger()
            }
        }

    }

    private fun createPublicationAsPassenger() {
        pubRepo = PublicationsRepository(requireContext())
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val newPub = NewPublicationAsPassenger()
        newPub.uid = uid
        newPub.startLatitute = model.getStartLatitude()
        newPub.startLongitude = model.getStartLongitude()
        newPub.endLatitute = model.getEndLatitude()
        newPub.endLongitude = model.getEndLongitude()
        newPub.date = model.getDate()
        newPub.time = model.getTime()
        newPub.type = model.getType().toString()
        newPub.acceptDoc = model.getAcceptDoc()
        newPub.acceptAlunos = model.getAcceptAlunos()

        pubRepo.createPublicationAsPassenger(newPub) {
            removeAllFragments()
            Toast.makeText(requireContext(), "Boleia criada com sucesso!", Toast.LENGTH_LONG).show()
        }
    }

    private fun removeAllFragments() {
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

        supportFragmentManager.beginTransaction().remove(fragCreatePubSearchStartPos!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubSearchEndPos!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickDate!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickTime!!).commit()
        supportFragmentManager.beginTransaction().remove(fragCreatePubPickPlaces!!).commit()
        supportFragmentManager.beginTransaction().remove(this).commit()

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationSetDefinitionsPassengerFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}