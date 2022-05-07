package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.FragmentMyActiveRidesFragmentBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.history.RVmyActiveRidesAdapter
import com.example.ipcaboleias.rides.Ride


class MyActiveRidesFragmentFragment : Fragment(R.layout.fragment_my_active_rides_fragment) {
    private var _binding: FragmentMyActiveRidesFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var myRides: MutableList<Ride>

    private lateinit var pubRepo: PublicationsRepository
    private lateinit var adapter: RVmyActiveRidesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMyActiveRidesFragmentBinding.bind(view)
        pubRepo = PublicationsRepository(requireContext())
        myRides = ArrayList()

        adapter = RVmyActiveRidesAdapter(myRides)


        binding.apply {
            pubRepo.getCurrentUserActivePublications {
                for(ride in it){
                    myRides.add(ride)
                }

                updateRecyclerView()
            }
        }

    }

    private fun updateRecyclerView(){
        val rvMyActiveRides = requireActivity().findViewById<RecyclerView>(R.id.rvMyActivePublications)

        rvMyActiveRides.layoutManager = LinearLayoutManager(activity)
        adapter = RVmyActiveRidesAdapter(myRides)
        rvMyActiveRides.adapter = adapter
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MyActiveRidesFragmentFragment().apply {

            }
    }
}
