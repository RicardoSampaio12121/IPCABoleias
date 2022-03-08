package com.example.ipcaboleias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RidesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RidesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }




    }

    override fun onCreateView(
        

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rides, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = requireView().findViewById<RecyclerView>(R.id.rvPublications)

        var publicationList = mutableListOf(
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F),
            RVPublication("Ricardo", "Braga", "Barcelos", "24/08/2022", 4.0F),
            RVPublication("José", "Porto", "Braga", "27/08/2022", 9.0F),
            RVPublication("Sampaio", "Esposende", "Guimaraes", "29/08/2022", 12.4F)
        )

        val adapter = RVPublicationsAadapter(publicationList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(activity)
        adapter.setOnItemClickListener(object : RVPublicationsAadapter.onItemClickListener{
            override fun onItemClick(position: Int){
                //Toast.makeText(applicationContext, "Item clicado: $position", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RidesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RidesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}