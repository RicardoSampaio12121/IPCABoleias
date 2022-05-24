package com.example.ipcaboleias.chat

import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.Utils.Utils
import com.example.ipcaboleias.databinding.ItemChatChannelBinding
import com.example.ipcaboleias.firebaseRepository.ChatChannelPresentation
import com.example.ipcaboleias.rides.RVPublicationsAadapter
import java.util.*

class RVChatChannelsAdapter(var channels: MutableList<ChatChannelPresentation>) :
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RVChatChannelsAdapter.ToDoViewHolder, position: Int) {
        val utils = Utils()

        holder.binding.apply {
            tvName.text = "${channels[position].name} ${channels[position].surname}"

            if (utils.isStudent(channels[position].email)) {
                tvRole.text = "Estudante do IPCA"
            } else {
                tvRole.text = "Docente do IPCA"
            }

            profilePic.setImageBitmap(utils.stringToBitMapPicture(channels[position].profilePicture))
        }
    }

    override fun getItemCount(): Int {
        return channels.size
    }
}