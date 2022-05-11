package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ipcaboleias.databinding.FragmentEditPublicationBinding

private const val Id = "id"

class EditPublicationFragment : Fragment(R.layout.fragment_edit_publication) {

    private var _binding: FragmentEditPublicationBinding? = null
    private val binding get() = _binding!!

    private var _id: String? = null

    private val EDIT_PUB_DATE_TIME_FRAG_TAG = "editPubDateTimeFragTag"
    private val EDIT_PUB_PRICE_FRAG_TAG = "editPubPriceFragTag"
    private val EDIT_PUB_PLACES_FRAG_TAG = "editPubPlacesFragTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            _id = it.getString(Id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentEditPublicationBinding.bind(view)

        val supportFragmentManager = requireActivity().supportFragmentManager

        binding.apply {
            returnButton.setOnClickListener {
                supportFragmentManager.beginTransaction().remove(this@EditPublicationFragment)
                    .commit()
            }

            viewDateTime.setOnClickListener {
                supportFragmentManager.beginTransaction().add(
                    R.id.frameFragment,
                    EditDateTimeFragment.newInstance(_id!!),
                    EDIT_PUB_DATE_TIME_FRAG_TAG
                ).commit()
            }

            viewPrice.setOnClickListener {
                supportFragmentManager.beginTransaction().add(
                    R.id.frameFragment,
                    EditPriceFragment.newInstance(_id!!),
                    EDIT_PUB_PRICE_FRAG_TAG
                ).commit()
            }

            viewPlaces.setOnClickListener {
                supportFragmentManager.beginTransaction().add(
                    R.id.frameFragment,
                    EditPlacesFragment.newInstance(_id!!),
                    EDIT_PUB_PLACES_FRAG_TAG
                ).commit()
            }

            viewDescription.setOnClickListener {

            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(id: String) =
            EditPublicationFragment().apply {
                arguments = Bundle().apply {
                    putString(Id, id)
                }
            }
    }
}