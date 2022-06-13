package com.example.ipcaboleias.dateTimePickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(requireActivity(), this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        var selectedTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendar.time)

        val selectedTimeBundle = Bundle()
        selectedTimeBundle.putString("SELECTED_TIME", selectedTime)

        setFragmentResult("REQUEST_KEY", selectedTimeBundle)
    }

}