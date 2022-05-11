package com.example.ipcaboleias.rides

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ipcaboleias.databinding.ItemPublicationV2Binding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern


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

            val location = getLocation(
                txtName.context,
                publications[position].startLatitude,
                publications[position].startLongitude
            )

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

            if (Pattern.matches("a[0-9]+@alunos.ipca.pt", publications[position].email)) {
                docStu.text = "Aluno do IPCA"
            } else {
                docStu.text = "Docente do IPCA"
            }

            if (location == null) {
                textFrom.text = "ola mundo"
            } else {
                textFrom.text = location.getAddressLine(0)
            }
            val data = LocalDate.parse(
                publications[position].date,
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            )

            val d = Date.from(data.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
            val local = Locale("pt", "BR")
            val formato: DateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", local)

            date.text = formato.format(d)

            val byteArray: ByteArray =
                Base64.getDecoder().decode(publications[position].profilePicture)
            val bitMapPic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            profilePic.setImageBitmap(bitMapPic)

        }
    }

    fun getLocation(context: Context, latitude: Double, longitude: Double): Address? {
        val addresses: MutableList<Address>
        val geocoder = Geocoder(context, Locale.ENGLISH)

        addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if(addresses.size == 0){
            return null
        }

        return addresses[0]
    }

    override fun getItemCount(): Int {

        return publications.size
    }


}