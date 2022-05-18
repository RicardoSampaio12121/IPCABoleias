package com.example.ipcaboleias.history

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemMyActiveRideBinding
import com.example.ipcaboleias.firebaseRepository.RidesWithDocId
import com.example.ipcaboleias.rides.Ride
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class RVmyActiveRidesAdapter(
    var rides: MutableList<RidesWithDocId>,
    var optionsMenuClickListener: OptionsMenuClickListener,
    var seePassengersClickListener: SeePassengersClickListener
) :
    RecyclerView.Adapter<RVmyActiveRidesAdapter.ToDoViewHolder>() {

    private lateinit var mListener: RVmyActiveRidesAdapter.onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }

    interface SeePassengersClickListener {
        fun onSeePassengersClickListener(position: Int)
    }

    inner class ToDoViewHolder(
        val binding: ItemMyActiveRideBinding,
        listener: onItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    fun removeItem(position: Int) {
        rides.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemMyActiveRideBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding, mListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {

            val timestamp = rides[position].dateTime
            val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
            val localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault())

            val localDate = localDateTime.toLocalDate()
            val localTime = localDateTime.toLocalTime()

            val d = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
            val local = Locale("pt", "BR")
            val formato: DateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", local)

            txtDate.text = "${formato.format(d)} às ${localTime.hour}:${localTime.minute} h"

            val location = getLocation(
                txtDate.context,
                rides[position].startLatitute,
                rides[position].startLongitude
            )

            if (location == null) {
                txtFrom.text = "null"
            } else {
                txtFrom.text = location.getAddressLine(0)
            }


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

            textViewOptions.setOnClickListener {
                optionsMenuClickListener.onOptionsMenuClicked(position)
            }

            view8.setOnClickListener {
                seePassengersClickListener.onSeePassengersClickListener(position)
            }

        }
    }

    fun getLocation(context: Context, latitude: Double, longitude: Double): Address? {
        val addresses: MutableList<Address>
        val geocoder = Geocoder(context, Locale.ENGLISH)

        addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses.size == 0) {
            return null
        }

        return addresses[0]
    }

    override fun getItemCount(): Int {
        return rides.size
    }
}