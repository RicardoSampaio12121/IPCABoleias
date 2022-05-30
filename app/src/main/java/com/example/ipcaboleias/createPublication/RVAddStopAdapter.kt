package com.example.ipcaboleias.createPublication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.Utils.Utils
import com.example.ipcaboleias.databinding.ItemAddStopBinding

class RVAddStopAdapter(var stops: MutableList<NewStop>) :
    RecyclerView.Adapter<RVAddStopAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(
        val binding: ItemAddStopBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAddStopBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {
            val utils = Utils()

            val location = utils.getLocation(
                tvPlaceName.context,
                stops[position].latitude,
                stops[position].longitude
            )

            tvPlaceName.text = location!!.getAddressLine(0)
        }
    }

    override fun getItemCount(): Int {
        return stops.size
    }
}