package com.example.ipcaboleias

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemPendingRequestBinding
import com.example.ipcaboleias.databinding.ItemScheduledRidesBinding
import java.util.*

class RVScheduledRidesAdapter(val scheduledRidesPresentations: MutableList<ScheduledRidePresentation>) :
    RecyclerView.Adapter<RVScheduledRidesAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(
        val binding: ItemScheduledRidesBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemScheduledRidesBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {
            val ride = scheduledRidesPresentations[position]

            tvName.text = ride.name

            if(ride.doc){
                tvRole.text = "Docente do IPCA"
            }
            else{
                tvRole.text = "Aluno do IPCA"
            }

            tvFrom.text = getLocation(
                tvDate.context,
                ride.startLat,
                ride.startLong
            ).getAddressLine(0)

            tvTo.text = getLocation(
                tvDate.context,
                ride.endLat,
                ride.endLong
            ).getAddressLine(0)
        }
    }

    fun getLocation(context: Context, latitude: Double, longitude: Double): Address {
        val addresses: MutableList<Address>
        val geocoder = Geocoder(context, Locale.ENGLISH)

        addresses = geocoder.getFromLocation(latitude, longitude, 1)

        return addresses[0]
    }

    override fun getItemCount(): Int {
        return scheduledRidesPresentations.size
    }

}