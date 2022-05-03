package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ipcaboleias.databinding.FragmentMyActiveRidesFragmentBinding


class MyActiveRidesFragmentFragment : Fragment(R.layout.fragment_my_active_rides_fragment) {
    private var _binding: FragmentMyActiveRidesFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMyActiveRidesFragmentBinding.bind(view)

        binding.apply {

        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MyActiveRidesFragmentFragment().apply {

            }
    }
}
