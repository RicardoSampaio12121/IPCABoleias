package com.example.ipcaboleias.createPublication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.dateTimePickers.DatePickerFragment
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationPickDateBinding


class CreatePublicationPickDateFragment : Fragment(R.layout.fragment_create_publication_pick_date) {
    private var _binding : FragmentCreatePublicationPickDateBinding? = null
    private val binding get() = _binding!!

    private val CREATE_PUB_PICK_DATE_FRAG_TAG = "createPubPickDateFragTag"
    private val CREATE_PUB_PICK_TIME_FRAG_TAG = "createPubPickTimeFragTag"

    private val model: NewPubViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationPickDateBinding.bind(view)

        binding.apply {

            tvDate.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        tvDate.text = date
                    }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }

            btnNext.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager


                model.setDate(tvDate.text.toString())

                val fragToHide = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_DATE_FRAG_TAG)
                val fragToCall = supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_TIME_FRAG_TAG)

                if(fragToCall != null){
                    supportFragmentManager.beginTransaction().show(fragToCall).commit()
                }
                else{
                    supportFragmentManager.beginTransaction().add(
                        R.id.frameLayoutFilter,
                        CreatePublicationPickTimeFragment.newInstance(), CREATE_PUB_PICK_TIME_FRAG_TAG).commit()
                }

                supportFragmentManager.beginTransaction().hide(fragToHide!!).commit()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationPickDateFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}