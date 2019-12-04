package com.venkatesh.chat

import java.util.*

data class ChatMessage(
    val messageText: String? = "",
    val messageUser: String? = ""
) {

    var messageTime: Long = Date().time
}