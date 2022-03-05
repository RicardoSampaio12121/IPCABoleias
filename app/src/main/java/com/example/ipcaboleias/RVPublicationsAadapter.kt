package com.example.ipcaboleias

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemPublicationBinding

//class RVPublicationsAadapter(
//    var publications: List<RVPublication>
//) : RecyclerView.Adapter<RVPublicationsAadapter.RVPublicationViewHolder>() {
//
//    inner class RVPublicationViewHolder(val binding: itemPublicationBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVPublicationViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_publication, parent, false)
//        return RVPublicationViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RVPublicationViewHolder, position: Int) {
//        holder.itemView.
//
//    }
//
//    override fun getItemCount(): Int {
//        return publications.size
//    }
//}

class RVPublicationsAadapter(var publications: List<RVPublication>): RecyclerView.Adapter<RVPublicationsAadapter.ToDoViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position : Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    inner class ToDoViewHolder(val binding: ItemPublicationBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root){
        init {

            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemPublicationBinding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding, mListener)

    }





    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {

        holder.binding.apply {
            tvName.text = publications[position].name
            tvPartida.text = publications[position].from
            tvChegada.text = publications[position].to
        }
    }



    override fun getItemCount(): Int {

        return  publications.size

    }



}