package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.databinding.FragmentEditPriceBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository

private const val Id = "id"

class EditPriceFragment : Fragment(R.layout.fragment_edit_price) {
    private var _binding: FragmentEditPriceBinding? = null
    private val binding get() = _binding!!

    private val model: PublicationDetailsViewModel by activityViewModels()

    private lateinit var pubRepo: PublicationsRepository

    private var _id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _id = it.getString(Id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditPriceBinding.bind(view)

        val supportFragmentManager = requireActivity().supportFragmentManager


        binding.apply {

            var ride = model.getRide()

            val maxPrice = ride.price!! + 3
            val minPrice = ride.price!! - 3

            tvNumber.text = String.format("%.2f", ride.price)

            returnButton.setOnClickListener {
                supportFragmentManager.beginTransaction().remove(this@EditPriceFragment).commit()
            }

            btnLess.setOnClickListener {
                if (ride.price!! - 1 >= minPrice) {
                    ride.price = ride.price!! - 1
                    tvNumber.text = String.format("%.2f", ride.price)
                }
            }

            btnMore.setOnClickListener {
                if (ride.price!! + 1 <= maxPrice) {
                    ride.price = ride.price!! + 1
                    tvNumber.text = String.format("%.2f", ride.price)
                }
            }

            btnSaveChanges.setOnClickListener {
                model.setRide(ride)
                pubRepo = PublicationsRepository(requireContext())

                pubRepo.editPrice(_id!!, ride.price!!) {
                    supportFragmentManager.beginTransaction().remove(this@EditPriceFragment)
                        .commit()
                }

            }
        }

    }


    companion object {

        @JvmStatic
        fun newInstance(id: String) =
            EditPriceFragment().apply {
                arguments = Bundle().apply {
                    putString(Id, id)
                }
            }
    }
}