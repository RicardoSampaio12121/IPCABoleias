package com.example.ipcaboleias.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemMyActiveRideBinding
import com.example.ipcaboleias.rides.Ride

class RVmyActiveRidesAdapter(var rides: MutableList<Ride>) :
    RecyclerView.Adapter<RVmyActiveRidesAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(val binding: ItemMyActiveRideBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemMyActiveRideBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {

        }
    }

    override fun getItemCount(): Int {
        return rides.size
    }
}