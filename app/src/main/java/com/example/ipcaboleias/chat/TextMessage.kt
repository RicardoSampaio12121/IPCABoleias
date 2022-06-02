package com.example.ipcaboleias.chat

import java.util.*

data class TextMessage(
    val message: String,
    val time: Date,
    val senderId: String,
    val senderName: String,
    val recipientId: String
) {

    constructor() : this("", Date(0), "", "", "")
}