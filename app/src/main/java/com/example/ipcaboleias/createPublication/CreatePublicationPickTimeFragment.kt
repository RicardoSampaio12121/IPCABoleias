package com.example.ipcaboleias.createPublication

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.R
import com.example.ipcaboleias.dateTimePickers.TimePickerFragment
import com.example.ipcaboleias.ViewModels.NewPubViewModel
import com.example.ipcaboleias.databinding.FragmentCreatePublicationPickTimeBinding
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class CreatePublicationPickTimeFragment : Fragment(R.layout.fragment_create_publication_pick_time) {

    var _binding: FragmentCreatePublicationPickTimeBinding? = null
    val binding get() = _binding!!

    private val CREATE_PUB_PICK_TIME_FRAG_TAG = "createPubPickTimeFragTag"
    private val CREATE_PUB_PICK_PLACES_FRAG_TAG = "createPubPickPlacesFragTag"


    private val model: NewPubViewModel by activityViewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCreatePublicationPickTimeBinding.bind(view)

        binding.apply {
            tvTime.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager

                val timePickerFragment = TimePickerFragment()

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val time = bundle.getString("SELECTED_TIME")
                        tvTime.text = time
                    }
                }
                timePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }

            btnNext.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager

                var formatter = DateTimeFormatter.ofPattern("H:mm")
                var time = LocalTime.parse(tvTime.text.toString(), formatter)

                if (model.getDate() == LocalDate.now()) {
                    if (time < LocalTime.now()) {
                        Toast.makeText(
                            requireContext(),
                            "Hora tem de ser posterior à atual",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@setOnClickListener
                    }
                }

                model.setTime(time)

                val fragToHide =
                    supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_TIME_FRAG_TAG)
                val fragToCall =
                    supportFragmentManager.findFragmentByTag(CREATE_PUB_PICK_PLACES_FRAG_TAG)

                if (fragToCall != null) {
                    supportFragmentManager.beginTransaction().show(fragToCall).commit()
                } else {
                    supportFragmentManager.beginTransaction().add(
                        R.id.frameLayoutFilter,
                        CreatePublicationPickPlacesFragment.newInstance(),
                        CREATE_PUB_PICK_PLACES_FRAG_TAG
                    ).commit()
                }

                supportFragmentManager.beginTransaction().hide(fragToHide!!).commit()

            }


        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePublicationPickTimeFragment().apply {

            }
    }
}