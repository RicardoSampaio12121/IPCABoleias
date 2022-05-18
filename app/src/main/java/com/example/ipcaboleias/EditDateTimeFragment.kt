package com.example.ipcaboleias

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.databinding.FragmentEditDateTimeBinding
import com.example.ipcaboleias.dateTimePickers.DatePickerFragment
import com.example.ipcaboleias.dateTimePickers.TimePickerFragment
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

private const val Id = "id"

class EditDateTimeFragment : Fragment(R.layout.fragment_edit_date_time) {
    private var _binding: FragmentEditDateTimeBinding? = null
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentEditDateTimeBinding.bind(view)

        val supportFragmentManager = requireActivity().supportFragmentManager

        binding.apply {
            val ride = model.getRide()

            val timestamp = ride.date
            val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
            val localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault())

            txtDate.text = "${localDateTime.dayOfMonth}/${localDateTime.month}/${localDateTime.year}"
            tvTime.text = "${localDateTime.hour}:${localDateTime.minute}"

            returnButton.setOnClickListener {
                supportFragmentManager.beginTransaction().remove(this@EditDateTimeFragment).commit()
            }

            viewDate.setOnClickListener {
                val datePickerFragment = DatePickerFragment()

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        txtDate.text = date
                    }
                }
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }

            viewTime.setOnClickListener {
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

            btnSaveChanges.setOnClickListener {
                pubRepo = PublicationsRepository(requireContext())

                pubRepo.editDateTime(_id!!, txtDate.text.toString(), tvTime.text.toString()) {
                    if (it) {
                        supportFragmentManager.beginTransaction().remove(this@EditDateTimeFragment)
                            .commit()
                    }
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            EditDateTimeFragment().apply {
                arguments = Bundle().apply {
                    putString(Id, id)
                }
            }
    }
}