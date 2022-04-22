package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.createPublication.CreatePublicationPickPriceFragment
import com.example.ipcaboleias.databinding.FragmentCreatePublicationAddDescriptionBinding
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
            stVU.setOnClickListener {
                stVS.isChecked = !stVU.isChecked
            }

            stVS.setOnClickListener{
                stVU.isChecked = !stVS.isChecked
            }

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
                model.setUniqueDrive(stVU.isChecked)
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