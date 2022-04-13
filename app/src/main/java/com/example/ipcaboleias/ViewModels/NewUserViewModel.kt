package com.example.ipcaboleias.ViewModels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewUserViewModel : ViewModel() {
   val name = MutableLiveData<String>()
   val surname = MutableLiveData<String>()
   val email = MutableLiveData<String>()
   val password = MutableLiveData<String>()
   val carBrand = MutableLiveData<String?>()
   val carModel = MutableLiveData<String?>()
   val carColor = MutableLiveData<String?>()
   val picture = MutableLiveData<Bitmap>()


   fun setName(newName: String?){
      name.value = newName!!
   }
   fun getName() : String{
      return name.value.toString()
   }

   fun setSurname(newSurname: String?){
      surname.value = newSurname!!
   }
   fun getSurname() : String{
      return surname.value.toString()
   }

   fun setEmail(newEmail: String?){
      email.value = newEmail!!
   }
   fun getEmail() : String{
      return email.value.toString()
   }

   fun setPassword(newPassword: String?){
      password.value = newPassword!!
   }
   fun getPassword() : String{
      return password.value.toString()
   }

   fun setCarBrand(newCarBrand: String?){
      carBrand.value = newCarBrand!!
   }
   fun getCarBrand() : String{
      return carBrand.value.toString()
   }

   fun setCarModel(newCarModel: String?){
      carModel.value = newCarModel!!
   }
   fun getCarModel() : String{
      return carModel.value.toString()
   }

   fun setCarColor(newCarColor: String?){
      carColor.value = newCarColor!!
   }
   fun getCarColor() : String{
      return carColor.value.toString()
   }

   fun setPicture(newPic: Bitmap?){
      picture.value = newPic!!
   }
   fun getPicture() : Bitmap{
      return picture.value!!
   }


}