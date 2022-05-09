package com.example.ipcaboleias.rides

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemPublicationV2Binding
import java.util.*


class RVPublicationsAadapter(var publications: MutableList<RidePresentation>) :
    RecyclerView.Adapter<RVPublicationsAadapter.ToDoViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }


    inner class ToDoViewHolder(
        val binding: ItemPublicationV2Binding,
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

        val binding = ItemPublicationV2Binding.inflate(layoutInflater, parent, false)

        return ToDoViewHolder(binding, mListener)

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {

        holder.binding.apply {

            txtName.text = publications[position].name
            textView2.text = publications[position].car

            if (publications[position].price == 0.0f) {
                textMoney.text = ""
            } else {
                textMoney.text = String.format("%.2f€", publications[position].price)
            }

            if (publications[position].places == 0) {
                textView4.text = ""
            } else {
                textView4.text = "${publications[position].places} lugares"
            }

            when (publications[position].endLatitude) {
                41.536587 -> {
                    textTo.text = "IPCA Barcelos"
                }
                41.542142 -> {
                    textTo.text = "IPCA Braga"
                }
                41.507823 -> {
                    textTo.text = "IPCA Guimarães"
                }
                41.440063 -> {
                    textTo.text = "IPCA Famalicão"
                }
            }

            textFrom.text = publications[position].startLatitude.toString()

            tvDateFrom.text = publications[position].time

            tvDateFrom.text = publications[position].date

            val byteArray: ByteArray =
                Base64.getDecoder().decode(publications[position].profilePicture)
            val bitMapPic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            profilePic.setImageBitmap(bitMapPic)

        }
    }

    override fun getItemCount(): Int {

        return publications.size
    }


}