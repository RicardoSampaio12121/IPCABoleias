package com.example.ipcaboleias.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemMyActiveRideBinding
import com.example.ipcaboleias.rides.Ride

class RVmyActiveRidesAdapter(
    var rides: MutableList<Ride>,
    var optionsMenuClickListener: OptionsMenuClickListener
) :
    RecyclerView.Adapter<RVmyActiveRidesAdapter.ToDoViewHolder>() {

    private lateinit var mListener: RVmyActiveRidesAdapter.onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
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

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {
            txtDate.text = rides[position].date
            txtFrom.text = rides[position].startLatitute.toString()

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

        }
    }

    override fun getItemCount(): Int {
        return rides.size
    }
}