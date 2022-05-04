package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ipcaboleias.databinding.FragmentMyActiveRidesFragmentBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository


class MyActiveRidesFragmentFragment : Fragment(R.layout.fragment_my_active_rides_fragment) {
    private var _binding: FragmentMyActiveRidesFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var pubRepo: PublicationsRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMyActiveRidesFragmentBinding.bind(view)
        pubRepo = PublicationsRepository(requireContext())

        binding.apply {
            pubRepo.getCurrentUserActivePublications {
            }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MyActiveRidesFragmentFragment().apply {

            }
    }
}
