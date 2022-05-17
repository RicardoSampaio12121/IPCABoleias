package com.example.ipcaboleias

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemPendingRequestBinding
import com.example.ipcaboleias.firebaseRepository.PublicationsRepository
import com.example.ipcaboleias.firebaseRepository.UsersRepository
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPendingRequestBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding)
    }

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
