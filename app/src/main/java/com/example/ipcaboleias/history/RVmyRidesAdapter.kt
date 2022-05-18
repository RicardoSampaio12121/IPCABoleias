package com.example.ipcaboleias.history

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemMyRideBinding
import com.example.ipcaboleias.rides.Ride
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class RVmyRidesAdapter(var rides: MutableList<Ride>) : RecyclerView.Adapter<RVmyRidesAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(val binding: ItemMyRideBinding) : RecyclerView.ViewHolder(binding.root){
        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemMyRideBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {
            val timestamp = rides[position].dateTime
            val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
            val localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault())

            val localDate = localDateTime.toLocalDate()

            val d = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
            val local = Locale("pt", "BR")
            val format: DateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", local)

            txtDate.text = format.format(d)

            val from = getLocation(txtDate.context, rides[position].startLatitute, rides[position].startLongitude)

            txtFrom.text = from.getAddressLine(0)

            when (rides[position].endLatitute) {
                41.536587 -> {
                    txtTo.text = "IPCA Barcelos"
                }
                41.542142 -> {
                    txtTo.text = "IPCA Braga"
                }
                41.507823 -> {
                    txtTo.text = "IPCA Guimarães"
                }
                41.440063 -> {
                    txtTo.text = "IPCA Famalicão"
                }
            }

            if(rides[position].status){
                tvState.text = "ativa"
            }
            else{
                tvState.text = "inativa"
            }
        }
    }

    fun getLocation(context: Context, latitude: Double, longitude: Double): Address {
        val addresses: MutableList<Address>
        val geocoder = Geocoder(context, Locale.ENGLISH)

        addresses = geocoder.getFromLocation(latitude, longitude, 1)

        return addresses[0]
    }

    override fun getItemCount(): Int {
        return rides.size
    }

}