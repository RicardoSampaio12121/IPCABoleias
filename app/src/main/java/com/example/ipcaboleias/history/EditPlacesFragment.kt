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
import com.example.ipcaboleias.databinding.FragmentEditPlacesBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository

private const val Id = "id"

class EditPlacesFragment : Fragment(R.layout.fragment_edit_places) {
    private var _binding: FragmentEditPlacesBinding? = null
    private val binding get() = _binding!!

    private var _id: String? = null

    private val model: PublicationDetailsViewModel by activityViewModels()

    private lateinit var pubRepo: PublicationsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _id = it.getString(Id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditPlacesBinding.bind(view)

        val supportFragmentManager = requireActivity().supportFragmentManager

        binding.apply {
            var ride = model.getRide()

            //TODO: BUG: Quando entro pela segunda vez, depois de ter guardado, aparece o número errado

            tvNumber.text = ride.places.toString()

            returnButton.setOnClickListener {
                supportFragmentManager.beginTransaction().remove(this@EditPlacesFragment).commit()
            }

            btnLess.setOnClickListener {
                val places = tvNumber.text.toString().toInt()

                if (places > 1)
                    tvNumber.text = (places - 1).toString()
            }

            btnMore.setOnClickListener {
                val places = tvNumber.text.toString().toInt()

                if (places < 4)
                    tvNumber.text = (places + 1).toString()
            }

            btnSaveChanges.setOnClickListener {
                pubRepo = PublicationsRepository(requireContext())
                ride.places = tvNumber.text.toString().toInt()
                model.setRide(ride)

                pubRepo.editPlaces(_id!!, ride.places) {
                    Toast.makeText(
                        requireContext(),
                        "Número de lugares editado com sucesso.",
                        Toast.LENGTH_LONG
                    ).show()

                    supportFragmentManager.beginTransaction().remove(this@EditPlacesFragment)
                        .commit()
                }

            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            EditPlacesFragment().apply {
                arguments = Bundle().apply {
                    putString(Id, id)
                }
            }
    }
}