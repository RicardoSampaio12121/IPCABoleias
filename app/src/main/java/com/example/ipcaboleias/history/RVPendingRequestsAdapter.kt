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
import com.example.ipcaboleias.Utils.Utils
import com.example.ipcaboleias.databinding.ItemPendingRequestBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

class RVPendingRequestsAdapter(
    val reserves: MutableList<ReservePresentation>,
    var acceptButtonClickListener: AcceptButtonClickListener,
    var chatButtonClickListener: ChatButtonClickListener,
    var rejectButtonClickListener: RejectButtonClickListener
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

    interface RejectButtonClickListener {
        fun onRejectButtonClickListener(position: Int)
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
        val utils = Utils()

        holder.binding.apply {

            tvName.text = reserves[position].name

            if (reserves[position].doc) {
                tvRole.text = "Docente do IPCA"
            } else {
                tvRole.text = "Aluno do IPCA"
            }

            tvFrom.text = utils.getLocation(
                tvDate.context,
                reserves[position].startLat,
                reserves[position].startLong
            )!!.getAddressLine(0)

            tvTo.text = utils.getIpcaCampus(reserves[position].endLat)

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

            val ptLocale: Locale = Locale("pt", "PT")
            val month = localDate.month.getDisplayName(TextStyle.FULL, ptLocale)

            tvDate.text =
                "${localDate.dayOfMonth} de ${month} de ${localDate.year} às ${localTime.hour}:${localTime.minute}h"


            btnAccept.setOnClickListener {
                acceptButtonClickListener.onAcceptButtonClickListener(position)
            }

            btnChat.setOnClickListener {
                chatButtonClickListener.onChatButtonClickListener(position)
            }

            btnReject.setOnClickListener {
                rejectButtonClickListener.onRejectButtonClickListener(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return reserves.size
    }
}
