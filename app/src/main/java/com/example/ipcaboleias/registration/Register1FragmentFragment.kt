package com.example.ipcaboleias.registration

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.NewUserViewModel
import com.example.ipcaboleias.databinding.FragmentRegister1FragmentBinding
import java.util.regex.Pattern

class Register1FragmentFragment : Fragment(R.layout.fragment_register1_fragment) {

    private val model: NewUserViewModel by activityViewModels()

    val REGISTER1_FRAG_TAG = "register1_frag_tag"
    val REGISTER2_FRAG_TAG = "register2_frag_tag"
    val REGISTER3_FRAG_TAG = "register3_frag_tag"


    private var _binding: FragmentRegister1FragmentBinding? = null
    private val binding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        _binding = FragmentRegister1FragmentBinding.bind(view)

        binding.apply {


            //TODO: Remover fragmento do carro se carregar
            cbHasCar.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager

                if (!cbHasCar.isChecked) {

                    model.carBrand.value = ""
                    model.carColor.value = ""
                    model.carModel.value = ""
                    model.carPlate.value = ""

                    val fragToRemove = supportFragmentManager.findFragmentByTag(REGISTER2_FRAG_TAG)

                    if (fragToRemove != null) {
                        supportFragmentManager.beginTransaction().remove(fragToRemove).commit()
                    }
                }
            }


            btnNext.setOnClickListener {
                val regex1 = "a[0-9]+@alunos.ipca.pt"
                val regex2 = "[a-z]+@ipca.pt"

                if (editTextName.text.isEmpty()) {
                    Toast.makeText(activity, "Tem de preencher o nome.", Toast.LENGTH_SHORT).show()
                    editTextName.requestFocus()

                } else if (editTextSurname.text.isEmpty()) {
                    Toast.makeText(activity, "Tem de preencher o sobrenome.", Toast.LENGTH_SHORT)
                        .show()
                    editTextSurname.requestFocus()
                } else if (editTextEmail.text.isEmpty()) {
                    Toast.makeText(activity, "Tem de preencher o email.", Toast.LENGTH_SHORT).show()
                    editTextEmail.requestFocus()
                } else if(!Pattern.matches(regex1, editTextEmail.text.toString())){
                    Toast.makeText(activity, "Email inválido.", Toast.LENGTH_SHORT).show()
                    editTextEmail.requestFocus()
                } else if (editTextPassword.text.toString() != editTextConfirmPassword.text.toString()){
                    Toast.makeText(activity, "Passwords não coindidem.", Toast.LENGTH_SHORT).show()
                    editTextPassword.requestFocus()
                }else {


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

                    if (cbHasCar.isChecked) {
                        var fragToCall =
                            supportFragmentManager.findFragmentByTag(REGISTER2_FRAG_TAG)

                        if (fragToCall != null) {
                            supportFragmentManager.beginTransaction().show(fragToCall).commit()
                        } else {
                            supportFragmentManager.beginTransaction().add(
                                R.id.fragmentContainer,
                                Register2Fragment.newInstance(),
                                REGISTER2_FRAG_TAG
                            ).commit()
                        }
                    } else {
                        var fragToCall =
                            supportFragmentManager.findFragmentByTag(REGISTER3_FRAG_TAG)

                        if (fragToCall != null) {
                            supportFragmentManager.beginTransaction().show(fragToCall).commit()
                        } else {
                            supportFragmentManager.beginTransaction().add(
                                R.id.fragmentContainer,
                                register3Fragment.newInstance(),
                                REGISTER3_FRAG_TAG
                            ).commit()
                        }
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



