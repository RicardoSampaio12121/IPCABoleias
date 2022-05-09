package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ipcaboleias.databinding.FragmentEditPublicationBinding

class EditPublicationFragment : Fragment(R.layout.fragment_edit_publication) {

    private var _binding: FragmentEditPublicationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentEditPublicationBinding.bind(view)

        val supportFragmentManager = requireActivity().supportFragmentManager

        binding.apply {
            returnButton.setOnClickListener {
                supportFragmentManager.beginTransaction().remove(this@EditPublicationFragment)
                    .commit()
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            EditPublicationFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}