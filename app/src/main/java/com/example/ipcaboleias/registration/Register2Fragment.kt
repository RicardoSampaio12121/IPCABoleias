package com.example.ipcaboleias.registration

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewUserViewModel
import com.example.ipcaboleias.databinding.FragmentRegister2Binding


class Register2Fragment : Fragment(R.layout.fragment_register2) {

    private val model: NewUserViewModel by activityViewModels()

    var REGISTER1_FRAG_TAG = "register1_frag_tag"
    var REGISTER2_FRAG_TAG = "register2_frag_tag"
    val REGISTER3_FRAG_TAG = "register3_frag_tag"

    private var _binding: FragmentRegister2Binding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        var btnReturn = activity?.requireViewById<ImageButton>(R.id.btnReturn)
//
//        btnReturn?.setOnClickListener{
//            val supportFragmentManager = activity?.supportFragmentManager
//            val fragToHide = (supportFragmentManager?.findFragmentByTag(REGISTER2_FRAG_TAG))!!
//            val fragToPop = supportFragmentManager.findFragmentByTag(REGISTER1_FRAG_TAG)!!
//
//            supportFragmentManager.beginTransaction().hide(fragToHide).commit()
//            supportFragmentManager.beginTransaction().show(fragToPop).commit()
//
//        }
        _binding = FragmentRegister2Binding.bind(view)

        binding.apply {
            btnRegister.setOnClickListener{

                //Finish activity after register is complete
                activity?.finish()
            }

            btnNext.setOnClickListener{
                val supportFragmentManager = requireActivity().supportFragmentManager

                model.setCarBrand(etBrand.text.toString())
                model.setCarModel(etModel.text.toString())
                model.setCarColor(etColor.text.toString())
                model.setCarPlate(etCarPlate.text.toString())

                //hide current fragment
                val currentFrag = supportFragmentManager.findFragmentByTag(REGISTER2_FRAG_TAG)
                supportFragmentManager.beginTransaction().hide(currentFrag!!).commit()

                //call next fragment
                val fragToCall = supportFragmentManager.findFragmentByTag(REGISTER3_FRAG_TAG)

                if(fragToCall == null){
                    supportFragmentManager.beginTransaction().add(
                        R.id.fragmentContainer,
                        register3Fragment.newInstance(), REGISTER3_FRAG_TAG).commit()
                }else{
                    supportFragmentManager.beginTransaction().show(fragToCall).commit()
                }
            }
        }


    }

    companion object {

        @JvmStatic
        fun newInstance() =
            Register2Fragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}