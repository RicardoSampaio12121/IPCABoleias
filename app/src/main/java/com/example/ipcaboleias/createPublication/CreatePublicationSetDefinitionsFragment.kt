package com.example.ipcaboleias.createPublication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationSetDefinitionsBinding

class CreatePublicationSetDefinitionsFragment : Fragment(R.layout.fragment_create_publication_set_definitions) {

    private var _binding : FragmentCreatePublicationSetDefinitionsBinding? = null
    val binding get() = _binding!!

    private val CREATE_PUB_SET_DEFINITIONS_FRAG_TAG = "createPubSetDefFragTag"
    private val CREATE_PUB_PICK_PRICE_FRAG_TAG = "createPubPickPriceFragTag"

    private val model: NewPubViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationSetDefinitionsBinding.bind(view)

        binding.apply {
            stDoc.setOnClickListener {
                if(!stDoc.isChecked && !stAlunos.isChecked){
                    stAlunos.isChecked = true
                }
            }

            stAlunos.setOnClickListener {
                if(!stDoc.isChecked && !stAlunos.isChecked){
                    stDoc.isChecked = true
                }
            }


            btnNext.setOnClickListener {
                model.setAcceptDoc(stDoc.isChecked)
                model.setAcceptAlunos(stAlunos.isChecked)

                val supportFragmentManager = requireActivity().supportFragmentManager

                val fragToHide = supportFragmentManager.findFragmentByTag(CREATE_PUB_SET_DEFINITIONS_FRAG_TAG)
                val fragToCall = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PRICE_FRAG_TAG)

                supportFragmentManager.beginTransaction().hide(fragToHide!!).commit()

                if(fragToCall != null){
                    supportFragmentManager.beginTransaction().show(fragToCall).commit()
                }
                else{
                    supportFragmentManager.beginTransaction().add(R.id.frameLayoutFilter, CreatePublicationPickPriceFragment.newInstance(), CREATE_PUB_PICK_PRICE_FRAG_TAG).commit()

                }
            }

        }
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationSetDefinitionsFragment().apply {  }
    }
}