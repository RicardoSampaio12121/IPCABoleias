package com.example.ipcaboleias

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.example.ipcaboleias.databinding.FragmentRegister1FragmentBinding

class Register1FragmentFragment : Fragment(R.layout.fragment_register1_fragment) {

    var REGISTER1_FRAG_TAG = "register1_frag_tag"
    var REGISTER2_FRAG_TAG = "register2_frag_tag"

    private var _binding: FragmentRegister1FragmentBinding? = null
    private val binding get() = _binding!!


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
        return inflater.inflate(R.layout.fragment_register1_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        _binding = FragmentRegister1FragmentBinding.bind(view)

//        var btnReturn = activity?.requireViewById<ImageButton>(R.id.btnReturn)
//
//        btnReturn?.setOnClickListener{
//            activity?.finish()
//        }

        binding.apply{
            cbHasCar.setOnClickListener{
                if(cbHasCar.isChecked){
                    btnNext.visibility = View.VISIBLE
                    btnRegister.visibility = View.GONE
                }
                else{
                    btnNext.visibility = View.GONE
                    btnRegister.visibility = View.VISIBLE
                }
            }

            btnNext.setOnClickListener{
                val supportFragmentManager = requireActivity().supportFragmentManager
                val currentFrag = supportFragmentManager.findFragmentByTag(REGISTER1_FRAG_TAG)

                // hide current fragment
                supportFragmentManager.beginTransaction().hide(currentFrag!!).commit()

                // call next fragment
                //TODO: setCustomAnimations

                var fragToCall = supportFragmentManager.findFragmentByTag(REGISTER2_FRAG_TAG)

                if(fragToCall != null){
                    supportFragmentManager.beginTransaction().show(fragToCall).commit()
                }
                else{
                    supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, Register2Fragment.newInstance(), REGISTER2_FRAG_TAG).commit()
                }
            }

            btnRegister.setOnClickListener{
                //Call function to register the new user

                //Finish activity after user is registered
                //TODO: perguntar ao professor se os fragmentos são removidos quando a atividade termina
                activity?.finish()
            }
        }



    }

    companion object {
        @JvmStatic
        fun newInstance() =
            Register1FragmentFragment().apply {
                arguments = Bundle().apply {

                }
            }

    }
}