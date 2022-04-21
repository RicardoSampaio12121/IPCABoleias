package com.example.ipcaboleias.firebaseRepository.Callbacks

import com.example.ipcaboleias.registration.NewUser

interface UserCallback {
    fun onCallback(user : NewUser)
}