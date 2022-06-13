package com.example.ipcaboleias.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.databinding.FragmentEditDescriptionBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository

private const val Id = "id"

class EditDescriptionFragment : Fragment(R.layout.fragment_edit_description) {
    private var _binding: FragmentEditDescriptionBinding? = null
    private val binding get() = _binding!!

    private lateinit var pubRepo: PublicationsRepository

    private var id: String? = null

    private val model: PublicationDetailsViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(Id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentEditDescriptionBinding.bind(view)

        val supportFragmentManager = requireActivity().supportFragmentManager

        val ride = model.getRide()

        binding.apply {
            etDescription.setText(ride.description)

            returnButton.setOnClickListener {
                supportFragmentManager.beginTransaction().remove(this@EditDescriptionFragment)
                    .commit()
            }

            btnSaveChanges.setOnClickListener {
                pubRepo = PublicationsRepository(requireContext())

                pubRepo.editDescription(etDescription.text.toString(), id!!) {
                    if (it) {
                        ride.description = etDescription.text.toString()
                        Toast.makeText(
                            requireContext(),
                            "Descrição atualizada com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Erro ao editar a descrição",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    supportFragmentManager.beginTransaction().remove(this@EditDescriptionFragment)
                        .commit()
                }

            }

        }

    }


    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            EditDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putString(Id, id)
                }
            }
    }
}