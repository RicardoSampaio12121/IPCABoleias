package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.ipcaboleias.databinding.FragmentFilterResultsBinding
import com.google.android.material.datepicker.MaterialDatePicker

class FilterResults : Fragment(R.layout.fragment_filter_results) {

    private var _binding: FragmentFilterResultsBinding? = null
    private val binding get() = _binding!!
    var FILTER_FRAG_TAG = "filterFragTag"

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
        return inflater.inflate(R.layout.fragment_filter_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        _binding = FragmentFilterResultsBinding.bind(view)

        binding.apply {

            frameReturnButton.setOnClickListener{

                val supportFragmentManager = requireActivity().supportFragmentManager
                val fragment = supportFragmentManager.findFragmentByTag(FILTER_FRAG_TAG)

                supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).hide(fragment!!).commit()
            }


            viewCalendar.setOnClickListener{

                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener("REQUEST_KEY",
                    viewLifecycleOwner
                ){
                    resultKey, bundle -> if (resultKey == "REQUEST_KEY"){
                        val date = bundle.getString("SELECTED_DATE")
                        tvDate.text = date
                }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
        }

        val FromAutoTextView = requireView().findViewById<AutoCompleteTextView>(R.id.FromAutoCompleteTV)
        val cities = resources.getStringArray(R.array.Cities)

        val adapter = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, cities)

        FromAutoTextView.setAdapter(adapter)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FilterResults().apply {
                arguments = Bundle().apply {

                }
            }
    }
}