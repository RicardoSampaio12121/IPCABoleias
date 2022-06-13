package com.example.ipcaboleias.history

import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemPassengerBinding
import com.google.firebase.installations.Utils
import com.google.type.LatLng
import java.util.*
import java.util.regex.Pattern

class RVPassengersAdapter(
    val passengers: MutableList<PassangerPresentation>,
    val endCoordinates: com.google.android.gms.maps.model.LatLng,
    val chatButtonClickListener: ChatButtonClickListener,
    val openMapClickListener: OpenMapClickListener
) :
    RecyclerView.Adapter<RVPassengersAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(val binding: ItemPassengerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    interface ChatButtonClickListener {
        fun onChatButtonClickListener(position: Int)
    }

    interface OpenMapClickListener {
        fun onOpenMapClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPassengerBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val utils = com.example.ipcaboleias.Utils.Utils()

        holder.binding.apply {
            txtNamePerson.text = "${passengers[position].name} ${passengers[position].surname}"

            if (Pattern.matches("a[0-9]+@alunos.ipca.pt", passengers[position].email)) {
                tvDocOrStu.text = "Aluno do IPCA"
            } else {
                tvDocOrStu.text = "Docente do IPCA"
            }

            val byteArray: ByteArray =
                Base64.getDecoder().decode(passengers[position].profilePicture)
            val bitMapPic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            framePerson.setImageBitmap(bitMapPic)

            btnChat.setOnClickListener {
                chatButtonClickListener.onChatButtonClickListener(position)
            }

            openMap.setOnClickListener {
                openMapClickListener.onOpenMapClickListener(position)
            }

            tvAddress.text = utils.getLocation(
                tvAddress.context,
                passengers[position].startLatitude,
                passengers[position].startLongitude
            )!!.getAddressLine(0)

            val start = com.google.android.gms.maps.model.LatLng(
                passengers[position].startLatitude,
                passengers[position].startLongitude
            )


            price.setText(String.format("%.2f €", utils.calculatePrice(start, endCoordinates)))

        }
    }

    override fun getItemCount(): Int {
        return passengers.size
    }
}