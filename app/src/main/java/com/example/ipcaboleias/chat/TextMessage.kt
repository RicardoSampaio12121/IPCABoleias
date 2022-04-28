package com.example.ipcaboleias.chat

import java.util.*

data class TextMessage(
    val message: String,
    val time: Date,
    val senderId: String
) {

    constructor() : this("", Date(0), "")
}