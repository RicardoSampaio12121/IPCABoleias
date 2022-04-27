package com.example.ipcaboleias.chat

data class ChatChannel(val usersIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}