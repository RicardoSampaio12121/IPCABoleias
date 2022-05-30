package com.example.ipcaboleias.history

import android.content.Context
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.ReservePresentation
import com.example.ipcaboleias.databinding.ItemPendingRequestBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class RVPendingRequestsAdapter(
    val reserves: MutableList<ReservePresentation>,
    var acceptButtonClickListener: AcceptButtonClickListener,
    var chatButtonClickListener: ChatButtonClickListener
) :
    RecyclerView.Adapter<RVPendingRequestsAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(
        val binding: ItemPendingRequestBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
        }
    }

    interface AcceptButtonClickListener {
        fun onAcceptButtonClickListener(position: Int)
    }

    interface ChatButtonClickListener {
        fun onChatButtonClickListener(position: Int)
    }

    fun removeItem(position: Int) {
        reserves.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPendingRequestBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {

            tvName.text = reserves[position].name

            if (reserves[position].doc) {
                tvRole.text = "Docente do IPCA"
            } else {
                tvRole.text = "Aluno do IPCA"
            }

            tvFrom.text = getLocation(
                tvDate.context,
                reserves[position].startLat,
                reserves[position].startLong
            ).getAddressLine(0)

            tvTo.text = getLocation(
                tvDate.context,
                reserves[position].endLat,
                reserves[position].endLong
            ).getAddressLine(0)

            val byteArray: ByteArray =
                Base64.getDecoder().decode(reserves[position].profilePicture)
            val bitMapPic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            circleImageView.setImageBitmap(bitMapPic)

            val timestamp = reserves[position].date
            val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
            val localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault())

            val localDate = localDateTime.toLocalDate()
            val localTime = localDateTime.toLocalTime()

            val d = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
            val local = Locale("pt", "BR")
            val formato: DateFormat =
                SimpleDateFormat("dd 'de' MMMM 'de' yyyy 'às' H ':' mm", local)

            tvDate.text =
                "${localDate.dayOfMonth} de ${localDate.month} de ${localDate.year} às ${localTime.hour}:${localTime.minute}h"


            btnAccept.setOnClickListener {
                acceptButtonClickListener.onAcceptButtonClickListener(position)
            }

            btnChat.setOnClickListener {
                chatButtonClickListener.onChatButtonClickListener(position)
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
        return reserves.size
    }
}
