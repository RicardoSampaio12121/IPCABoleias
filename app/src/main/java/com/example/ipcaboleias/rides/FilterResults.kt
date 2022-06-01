package com.example.ipcaboleias.rides

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.FilterResultsViewModel
import com.example.ipcaboleias.databinding.FragmentFilterResultsBinding
import com.example.ipcaboleias.dateTimePickers.DatePickerFragment

class FilterResults : Fragment(R.layout.fragment_filter_results) {

    private var _binding: FragmentFilterResultsBinding? = null
    private val binding get() = _binding!!

    private val filterModel: FilterResultsViewModel by activityViewModels()

    var FILTER_FRAG_TAG = "filterFragTag"
    private val RIDES_FRAG_TAG = "ridesFragTag"

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
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.IPCAPolos,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerSearchLocation.adapter = adapter
            }

            frameReturnButton.setOnClickListener {

                val supportFragmentManager = requireActivity().supportFragmentManager
                val fragment = supportFragmentManager.findFragmentByTag(FILTER_FRAG_TAG)

//                supportFragmentManager.beginTransaction().setCustomAnimations(
//                    R.anim.slide_in,
//                    R.anim.slide_out
//                ).hide(fragment!!).commit()

                supportFragmentManager.beginTransaction().hide(this@FilterResults).commit()
            }


            viewCalendar.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        tvDate.text = date
                    }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }

            SubmitButton.setOnClickListener {
                val location = spinnerSearchLocation.selectedItem.toString()

                when (location) {
                    "Polo de Barcelos" -> {
                        filterModel.toLatitude.value = 41.536587
                    }
                    "Polo de Braga" -> {
                        filterModel.toLatitude.value = 41.542142
                    }
                    "Polo de Guimarães" -> {
                        filterModel.toLatitude.value = 41.507823
                    }
                    "Polo de Famalicão" -> {
                        filterModel.toLatitude.value = 41.440063
                    }
                }

                filterModel.seeDriversRides.value = CondutoresSwitch.isChecked
                filterModel.seePassengersRides.value = PassageirosSwitch.isChecked
                filterModel.acceptProfessors.value = docenteSwithc.isChecked
                filterModel.acceptStudents.value = estudanteSwitch.isChecked

                filterModel.buttonClicked.value = !filterModel.buttonClicked.value!!

                requireActivity().supportFragmentManager.beginTransaction().hide(this@FilterResults)
                    .commit()
            }
        }

        val FromAutoTextView =
            requireView().findViewById<AutoCompleteTextView>(R.id.FromAutoCompleteTV)
        //val cities = resources.getStringArray(R.array.Cities)
        //val adapter = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, cities)
        //FromAutoTextView.setAdapter(adapter)
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