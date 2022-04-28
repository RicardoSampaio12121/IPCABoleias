package com.example.ipcaboleias.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemTextMessageBinding
import com.example.ipcaboleias.rides.RVPublicationsAadapter
import com.google.firebase.auth.FirebaseAuth

class RVChatMessagesAdapter(var messages : MutableList<TextMessage>) : RecyclerView.Adapter<RVChatMessagesAdapter.ToDoViewHolder>() {

    private lateinit var mListener : RVPublicationsAadapter.onItemClickListener

    inner class ToDoViewHolder(val binding: ItemTextMessageBinding, listener: RVPublicationsAadapter.onItemClickListener) : RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemTextMessageBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding, mListener)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {
            textViewMessageText.text = messages[position].message
            textViewMessageTime.text = messages[position].time.toString()

            if(messages[position].senderId == FirebaseAuth.getInstance().currentUser!!.uid){
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.gravity = Gravity.END
            }
            else{
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.gravity = Gravity.START
            }
        }
    }
}