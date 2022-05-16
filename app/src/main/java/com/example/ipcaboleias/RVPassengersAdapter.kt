package com.example.ipcaboleias

import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemPassengerBinding
import com.example.ipcaboleias.registration.NewUser
import java.util.*
import java.util.regex.Pattern

class RVPassengersAdapter(val passengers: MutableList<NewUser>) :
    RecyclerView.Adapter<RVPassengersAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(val binding: ItemPassengerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPassengerBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {
            txtNamePerson.text = passengers[position].name

            if (Pattern.matches("a[0-9]+@alunos.ipca.pt", passengers[position].email)) {
                tvDocOrStu.text = "Aluno do IPCA"
            } else {
                tvDocOrStu.text = "Docente do IPCA"
            }

            val byteArray: ByteArray =
                Base64.getDecoder().decode(passengers[position].profilePicture)
            val bitMapPic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            framePerson.setImageBitmap(bitMapPic)
        }
    }
    override fun getItemCount(): Int {
        return passengers.size
    }
}