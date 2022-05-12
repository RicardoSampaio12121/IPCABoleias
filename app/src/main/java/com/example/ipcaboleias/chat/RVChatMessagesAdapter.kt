package com.example.ipcaboleias.chat

import android.text.style.BackgroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.R
import com.example.ipcaboleias.databinding.ItemTextMessageBinding
import com.example.ipcaboleias.rides.RVPublicationsAadapter
import com.google.api.Distribution
import com.google.firebase.auth.FirebaseAuth

class RVChatMessagesAdapter(var messages : MutableList<TextMessage>) : RecyclerView.Adapter<RVChatMessagesAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(val binding: ItemTextMessageBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener{
                //listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemTextMessageBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {
            textViewMessageText.text = messages[position].message

            if(messages[position].senderId == FirebaseAuth.getInstance().currentUser!!.uid){
                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.gravity = Gravity.END

                messageRoot.gravity = Gravity.START
                messageRoot.setBackgroundColor(2)

            }
            else{
                val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.gravity = Gravity.START

                messageRoot.layoutParams = params



            }
        }
    }
}