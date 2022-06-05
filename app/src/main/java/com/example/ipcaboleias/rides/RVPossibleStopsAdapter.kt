package com.example.ipcaboleias.rides

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.Utils.Utils
import com.example.ipcaboleias.createPublication.NewStop
import com.example.ipcaboleias.databinding.ItemPossibleStopBinding
import com.google.type.LatLng

class RVPossibleStopsAdapter(var stops: MutableList<NewStop>, var startPosition: com.google.android.gms.maps.model.LatLng): RecyclerView.Adapter<RVPossibleStopsAdapter.ToDoViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }


    inner class ToDoViewHolder(
        val binding: ItemPossibleStopBinding,
        listener: onItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPossibleStopBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {

            val utils = Utils()
            val location = utils.getLocation(tvCity.context, stops[position].latitude, stops[position].longitude)

            val results = FloatArray(1)
            Location.distanceBetween(startPosition.latitude, startPosition.longitude, stops[position].latitude, stops[position].longitude, results)

            tvPlace.text = location!!.getAddressLine(0)
            tvCity.text = location.locality
            tvDistance.text = "A ${(results[0] / 1000)} km da posição inicial"
        }
    }

    override fun getItemCount(): Int {
        return stops.size
    }
}