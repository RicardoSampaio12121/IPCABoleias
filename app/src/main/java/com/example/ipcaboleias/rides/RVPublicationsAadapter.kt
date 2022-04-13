package com.example.ipcaboleias.rides

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemPublicationV2Binding


class RVPublicationsAadapter(var publications: List<RVPublication>): RecyclerView.Adapter<RVPublicationsAadapter.ToDoViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position : Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    inner class ToDoViewHolder(val binding: ItemPublicationV2Binding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root){
        init {

            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemPublicationV2Binding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding, mListener)

    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {

        holder.binding.apply {
            txtName.text = publications[position].name
            textMoney.text = publications[position].amount.toString() + " EUR"
            textFrom.text = publications[position].from
            textTo.text = publications[position].to
        }
    }

    override fun getItemCount(): Int {

        return  publications.size
    }



}