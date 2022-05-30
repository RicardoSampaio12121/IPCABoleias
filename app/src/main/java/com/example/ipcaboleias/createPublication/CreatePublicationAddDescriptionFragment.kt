package com.example.ipcaboleias.createPublication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationAddDescriptionBinding


class CreatePublicationAddDescriptionFragment : Fragment(R.layout.fragment_create_publication_add_description) {

    private var _binding : FragmentCreatePublicationAddDescriptionBinding? = null
    private val binding get() = _binding!!

    private val CREATE_PUB_ADD_DESCRIPTION_FRAG_TAG = "createPubAddDescriptionFragTag"
    private val CREATE_PUB_PICK_PRICE_FRAG_TAG = "createPubPickPriceFragTag"
    private val CREATE_PUB_SET_DEFINITIONS_FRAG_TAG = "createPubSetDefFragTag"


    private val model: NewPubViewModel by activityViewModels()

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationAddDescriptionFragment().apply {
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationAddDescriptionBinding.bind(view)

        val supportFragmentManager = requireActivity().supportFragmentManager

        binding.apply {
            btnNext.setOnClickListener {
                model.setDescription(etDescription.text.toString())

                val fragToHide = supportFragmentManager.findFragmentByTag(CREATE_PUB_ADD_DESCRIPTION_FRAG_TAG)
                val fragToCall = supportFragmentManager.findFragmentByTag(CREATE_PUB_SET_DEFINITIONS_FRAG_TAG)

                supportFragmentManager.beginTransaction().hide(fragToHide!!).commit()

                if(fragToCall != null){
                    supportFragmentManager.beginTransaction().show(fragToCall).commit()
                }
                else{
                    supportFragmentManager.beginTransaction().add(
                        R.id.frameLayoutFilter,
                        CreatePublicationSetDefinitionsFragment.newInstance(), CREATE_PUB_SET_DEFINITIONS_FRAG_TAG).commit()

                }
            }
        }
    }
}