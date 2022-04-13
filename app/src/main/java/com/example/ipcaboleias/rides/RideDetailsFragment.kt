package com.example.ipcaboleias.rides

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.example.ipcaboleias.R
import com.example.ipcaboleias.databinding.FragmentRideDetailsBinding


class RideDetailsFragment : Fragment(R.layout.fragment_ride_details) {

    private var _binding: FragmentRideDetailsBinding? = null
    private val binding get() = _binding!!

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
        return inflater.inflate(R.layout.fragment_ride_details, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        _binding = FragmentRideDetailsBinding.bind(view)

//        val descTitle = requireView().findViewById<TextView>(R.id.txtViewDescTitle)
//        val desc = requireView().findViewById<TextView>(R.id.txtDesc)
//        val view = requireView().findViewById<View>(R.id.viewDescription)
//        descTitle.visibility = View.GONE
//        desc.visibility = View.GONE
//        view.visibility = View.GONE

        var btnMenu = activity?.requireViewById<ImageButton>(R.id.imageMenu)
        var btnReturn = activity?.requireViewById<ImageButton>(R.id.imageReturn)

        btnMenu?.visibility = View.GONE
        btnReturn?.visibility = View.VISIBLE

        binding.apply {

        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RideDetailsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}