package com.example.ipcaboleias.rides

import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemPublicationV2Binding
import java.util.*


class RVPublicationsAadapter(var publications: MutableList<Ride>): RecyclerView.Adapter<RVPublicationsAadapter.ToDoViewHolder>() {

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {

        holder.binding.apply {

            txtName.text = publications[position].name
            textView2.text = publications[position].car

            if(publications[position].price == 0.0){
                textMoney.text = ""
            }
            else{
                textMoney.text = "${publications[position].price} €"
            }

            if(publications[position].places == 0){
                textView4.text = ""
            }
            else{
                textView4.text = "${publications[position].places} lugares"
            }

            textFrom.text = publications[position].startLatitude.toString()
            textTo.text = publications[position].endLatitude.toString()

            tvDateFrom.text = publications[position].time

            tvDateFrom.text = publications[position].date

            val byteArray : ByteArray = Base64.getDecoder().decode(publications[position].profilePicture)
            val bitMapPic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            profilePic.setImageBitmap(bitMapPic)

        }
    }

    override fun getItemCount(): Int {

        return  publications.size
    }



}