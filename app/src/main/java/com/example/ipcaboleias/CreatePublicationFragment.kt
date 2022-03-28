package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ipcaboleias.databinding.FragmentCreatePublicationBinding


class CreatePublicationFragment : Fragment(R.layout.fragment_create_publication) {

    private var _binding: FragmentCreatePublicationBinding? = null
    private val binding get() = _binding!!

    val CREATE_PUB_FRAG_TAG = "createPubFragTag"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_publication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationBinding.bind(view)

        binding.apply {
            frameReturnButton.setOnClickListener{
                val supportFragmentManager = requireActivity().supportFragmentManager
                val frag = supportFragmentManager.findFragmentByTag(CREATE_PUB_FRAG_TAG)!!

                supportFragmentManager.beginTransaction().remove(frag).commit()
            }

            btnLess.setOnClickListener{
                var number: Int = tvPlaces.text.toString().toInt()
                if(number > 1){
                    tvPlaces.text = (number - 1).toString()
                }
            }

            btnPlus.setOnClickListener {
                var number: Int = tvPlaces.text.toString().toInt()
                tvPlaces.text = (number + 1).toString()
            }

        }



    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}