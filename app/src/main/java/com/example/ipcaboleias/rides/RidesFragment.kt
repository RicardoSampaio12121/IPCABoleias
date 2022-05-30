package com.example.ipcaboleias.rides

import android.R.attr.data
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.R
import com.example.ipcaboleias.ViewModels.FilterResultsViewModel
import com.example.ipcaboleias.ViewModels.PublicationDetailsViewModel
import com.example.ipcaboleias.createPublication.CreatePublicationSearchStartLocationFragment
import com.example.ipcaboleias.databinding.FragmentRidesBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.UsersRepository
import com.example.ipcaboleias.registration.NewUser
import kotlinx.coroutines.*


class RidesFragment : Fragment(R.layout.fragment_rides) {

    private val CREATE_PUB_SEARCH1_FRAG_TAG = "createPubSearch1FragTag"

    private var _binding: FragmentRidesBinding? = null
    private val binding get() = _binding!!
    private lateinit var publications: MutableList<RidePresentation>
    var filteredPublications: MutableList<RidePresentation> = ArrayList()

    private lateinit var adapter: RVPublicationsAadapter
    private val model: PublicationDetailsViewModel by activityViewModels()
    private val filterModel: FilterResultsViewModel by activityViewModels()

    val FILTER_FRAG_TAG = "filterFragTag"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRidesBinding.bind(view)
        val pubRepo = PublicationsRepository(requireContext())
        val usersRepo = UsersRepository(requireContext())

        publications = ArrayList()
        adapter = RVPublicationsAadapter(publications)


        binding.apply {
            startFilterModel()
            filterListeners()

            // Evento click do botão para criar uma nova publicação
            btnAdd.setOnClickListener {
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.beginTransaction().add(
                    R.id.frameLayoutFilter,
                    CreatePublicationSearchStartLocationFragment.newInstance(),
                    CREATE_PUB_SEARCH1_FRAG_TAG
                ).commit()
            }

            //Carregar recycler view com as publicações

            //updateRecyclerView()

            pubRepo.getPublications { it ->
                for (ride in it) {
                    usersRepo.getUser(ride.uid) { user ->
                        publications.add(
                            newRidePresentationObject(
                                ride,
                                user
                            )
                        )
                        filteredPublications.clear()
                        filteredPublications.addAll(publications)
                        //adapter.notifyItemInserted(publications.size - 1)
                        updateRecyclerView()
                    }
                }
            }
        }
    }

    private fun startFilterModel() {
        filterModel.buttonClicked.value = false
        filterModel.acceptProfessors.value = false
        filterModel.acceptStudents.value = false
        filterModel.seeDriversRides.value = false
        filterModel.seePassengersRides.value = false
    }

    private fun filterListeners() {
        var list: MutableList<RidePresentation> = ArrayList()
        list.clear()

        filterModel.buttonClicked.observe(viewLifecycleOwner, Observer {
            list.clear()
            if (filterModel.seeDriversRides.value!! && !filterModel.seePassengersRides.value!!) { // Ver condutores
                if (filterModel.acceptStudents.value!! && !filterModel.acceptProfessors.value!!) { // Aceita alunos
                    for(pub in publications){
                        if(pub.type == "Driver" && pub.acceptAlunos && !pub.acceptDoc) list.add(pub)
                    }

                    updateRecyclerViewData(list)
                    return@Observer
                }

                if (!filterModel.acceptStudents.value!! && filterModel.acceptProfessors.value!!) { // Aceita professores
                    for(pub in publications){
                        if(pub.type == "Driver" && !pub.acceptAlunos && pub.acceptDoc) list.add(pub)
                    }

                    updateRecyclerViewData(list)
                    return@Observer
                }

                if(filterModel.acceptStudents.value!! && filterModel.acceptProfessors.value!!){ // Aceita ambos
                    for(pub in publications){
                        if(pub.type == "Driver" && pub.acceptAlunos && pub.acceptDoc) list.add(pub)
                    }

                    updateRecyclerViewData(list)
                    return@Observer
                }


            } else if (!filterModel.seeDriversRides.value!! && filterModel.seePassengersRides.value!!) { // Ver passageiros
                if (filterModel.acceptStudents.value!! && !filterModel.acceptProfessors.value!!) { // Aceita alunos
                    for(pub in publications){
                        if(pub.type == "Passenger" && pub.acceptAlunos && !pub.acceptDoc) list.add(pub)
                    }

                    updateRecyclerViewData(list)
                    return@Observer
                }
                if (!filterModel.acceptStudents.value!! && filterModel.acceptProfessors.value!!) { // Aceita professores
                    for(pub in publications){
                        if(pub.type == "Passenger" && !pub.acceptAlunos && pub.acceptDoc) list.add(pub)
                    }

                    updateRecyclerViewData(list)
                    return@Observer
                }
                if(filterModel.acceptStudents.value!! && filterModel.acceptProfessors.value!!) { // Aceita ambos
                    for(pub in publications){
                        if(pub.type == "Passenger" && pub.acceptAlunos && pub.acceptDoc) list.add(pub)
                    }

                    updateRecyclerViewData(list)
                    return@Observer
                }

            } else { // Ver ambos
                if (filterModel.acceptStudents.value!! && !filterModel.acceptProfessors.value!!) { // Aceita alunos
                    for(pub in publications){
                        if(pub.acceptAlunos && !pub.acceptDoc) list.add(pub)
                    }

                    updateRecyclerViewData(list)
                    return@Observer
                }
                if (!filterModel.acceptStudents.value!! && filterModel.acceptProfessors.value!!) { // Aceita professores
                    for(pub in publications){
                        if(!pub.acceptAlunos && pub.acceptDoc) list.add(pub)
                    }

                    updateRecyclerViewData(list)
                    return@Observer
                }
                if(filterModel.acceptStudents.value!! && filterModel.acceptProfessors.value!!) { // Aceita ambos
                    for(pub in publications){
                        if(pub.acceptAlunos && pub.acceptDoc) list.add(pub)
                    }

                    updateRecyclerViewData(list)
                    return@Observer
                }
            }
        })
    }

    private fun newRidePresentationObject(ride: Ride, user: NewUser): RidePresentation {
        return RidePresentation(
            ride.uid,
            user.name,
            user.email,
            "${user.carBrand} ${user.carModel}",
            user.carColor!!,
            user.profilePicture!!,
            ride.dateTime,
            ride.startLatitute,
            ride.startLongitude,
            ride.endLatitute,
            ride.endLongitude,
            ride.type,
            ride.places,
            ride.description,
            ride.acceptAlunos,
            ride.acceptDoc,
            ride.uniqueDrive,
            ride.price
        )
    }

    fun updateRecyclerViewData(data: MutableList<RidePresentation>) {

        println("Entra no updata data")
        println("Array length: ${data.size}")

        filteredPublications.clear()
        filteredPublications.addAll(data)
        adapter.notifyDataSetChanged()
    }

    fun updateRecyclerView() {
        val rvPublications = requireActivity().findViewById<RecyclerView>(R.id.rvPublications)

        rvPublications.layoutManager = LinearLayoutManager(activity)
        adapter = RVPublicationsAadapter(filteredPublications)
        rvPublications.adapter = adapter

        adapter.setOnItemClickListener(object : RVPublicationsAadapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                model.setRide(filteredPublications[position])

                val transaction = activity?.supportFragmentManager?.beginTransaction()

                if (filteredPublications[position].type == "Passenger") {
                    transaction?.add(
                        R.id.frameFragment,
                        RideDetailsPassengerFragment.newInstance(),
                        "detailsPassengerFragTag"
                    )?.commit()
                } else {
                    transaction?.add(
                        R.id.frameFragment,
                        RideDetailsFragment.newInstance(),
                        "detailsFragTag"
                    )?.commit()

                }

            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RidesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}