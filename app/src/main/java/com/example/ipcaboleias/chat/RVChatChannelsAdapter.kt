package com.example.ipcaboleias.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemChatChannelBinding
import com.example.ipcaboleias.rides.RVPublicationsAadapter

class RVChatChannelsAdapter(var channels: MutableList<Channel>) :
    RecyclerView.Adapter<RVChatChannelsAdapter.ToDoViewHolder>() {

    private lateinit var mListener: onItemClickListener


    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    inner class ToDoViewHolder(val binding: ItemChatChannelBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVChatChannelsAdapter.ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemChatChannelBinding.inflate(layoutInflater, parent, false)
        return ToDoViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: RVChatChannelsAdapter.ToDoViewHolder, position: Int) {
        holder.binding.apply {
            userId.text = channels[position].channelId
        }
    }

    override fun getItemCount(): Int {
        return channels.size
    }
}