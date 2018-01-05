package io.iconect.lightbot.domain.message.content

interface MessageContent<out T> {
    val message:String
    val content: T
}