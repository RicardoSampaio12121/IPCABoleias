package com.example.ipcaboleias.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewPubViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    val startLatitute = MutableLiveData<Double>()
    val startLongitude = MutableLiveData<Double>()
    val endLatitute = MutableLiveData<Double>()
    val endLongitude = MutableLiveData<Double>()
    val date = MutableLiveData<String>()
    val time = MutableLiveData<String>()
    val type = MutableLiveData<PubType>()
    val description = MutableLiveData<String>()
    val nPassengers = MutableLiveData<Int?>()
    val uniqueRide = MutableLiveData<Boolean>()
    val acceptDoc = MutableLiveData<Boolean>()
    val acceptAlunos = MutableLiveData<Boolean>()
    val price = MutableLiveData<Float>()

    fun setName(newName : String){
        name.value = newName
    }
    fun getName() : String{
        return name.value!!.toString()
    }

    fun setStartLatitude(newLatitude : Double){
        startLatitute.value = newLatitude
    }
    fun getStartLatitude() : Double{
        return startLatitute.value!!.toDouble()
    }

    fun setStartLongitude(newLongitude : Double){
        startLongitude.value = newLongitude
    }
    fun getStartLongitude() : Double{
        return startLongitude.value!!.toDouble()
    }

    fun setEndLatitude(newLatitude : Double){
        endLatitute.value = newLatitude
    }
    fun getEndLatitude() : Double{
        return endLatitute.value!!.toDouble()
    }

    fun setEndLongitude(newLongitude: Double){
        endLongitude.value = newLongitude
    }
    fun getEndLongitude() : Double{
        return endLongitude.value!!.toDouble()
    }

    fun setDate(newDate: String){
        date.value = newDate
    }
    fun getDate() : String{
        return date.value.toString()
    }

    fun getTime() : String{
        return time.value.toString()
    }
    fun setTime(newTime: String){
        time.value = newTime
    }

    fun getType() : PubType {
        return type.value!!
    }
    fun setType(newType : PubType){
        type.value = newType
    }

    fun setNPassengers(newNPassengers : Int?){
        nPassengers.value = newNPassengers
    }
    fun getNPassengers() : Int{
        return nPassengers.value!!
    }

    fun setPrice(newPrice : Float){
        price.value = newPrice
    }

    fun getPrice() : Float?{
        return price.value
    }

    fun setDescription(newDesc : String){
        description.value = newDesc
    }

    fun getDescription() : String{
        return description.value!!
    }

    fun setUniqueDrive(value : Boolean){
        uniqueRide.value = value
    }
    fun getUniqueDrive() : Boolean{
        return uniqueRide.value!!
    }

    fun setAcceptDoc(value : Boolean){
        acceptDoc.value = value
    }

    fun getAcceptDoc() : Boolean{
        return acceptDoc.value!!
    }
    fun setAcceptAlunos(value : Boolean){
        acceptAlunos.value = value
    }

    fun getAcceptAlunos() : Boolean{
        return acceptAlunos.value!!
    }




    enum class PubType{
        Driver, Passenger
    }


}