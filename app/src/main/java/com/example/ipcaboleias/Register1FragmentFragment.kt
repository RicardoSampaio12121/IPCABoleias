package com.example.ipcaboleias

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.ipcaboleias.databinding.FragmentRegister1FragmentBinding

class Register1FragmentFragment : Fragment(R.layout.fragment_register1_fragment) {

    private val model: NewUserViewModel by activityViewModels()

    val REGISTER1_FRAG_TAG = "register1_frag_tag"
    val REGISTER2_FRAG_TAG = "register2_frag_tag"
    val REGISTER3_FRAG_TAG = "register3_frag_tag"


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


            //TODO: Remover fragmento do carro se carregar
            cbHasCar.setOnClickListener {
                var supportFragmentManager = requireActivity().supportFragmentManager

                if(!cbHasCar.isChecked){
                    var fragToRemove = supportFragmentManager.findFragmentByTag(REGISTER2_FRAG_TAG)

                    if(fragToRemove != null){
                        supportFragmentManager.beginTransaction().remove(fragToRemove).commit()
                    }
                }
            }


            btnNext.setOnClickListener{
                //TODO: Verificar se campos são válidos/estão vazios


                val supportFragmentManager = requireActivity().supportFragmentManager
                val currentFrag = supportFragmentManager.findFragmentByTag(REGISTER1_FRAG_TAG)

                model.setName(editTextName.text.toString())
                model.setSurname(editTextSurname.text.toString())
                model.setEmail(editTextEmail.text.toString())
                model.setPassword(editTextPassword.text.toString())

                // hide current fragment
                supportFragmentManager.beginTransaction().hide(currentFrag!!).commit()

                // call next fragment
                //TODO: setCustomAnimations

                if(cbHasCar.isChecked){
                    var fragToCall = supportFragmentManager.findFragmentByTag(REGISTER2_FRAG_TAG)

                    if(fragToCall != null){
                        supportFragmentManager.beginTransaction().show(fragToCall).commit()
                    }
                    else{
                        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, Register2Fragment.newInstance(), REGISTER2_FRAG_TAG).commit()
                    }
                }else{
                    var fragToCall = supportFragmentManager.findFragmentByTag(REGISTER3_FRAG_TAG)

                    if(fragToCall != null){
                        supportFragmentManager.beginTransaction().show(fragToCall).commit()
                    }else{
                        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, register3Fragment.newInstance(), REGISTER3_FRAG_TAG).commit()
                    }
                }
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



