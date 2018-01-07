package io.iconect.lightbot.domain.message

interface ServerMessageRepository {

    fun retrieveMessages(): List<ServerMessage>

    fun sendMessage(channelIdentifier: String, message: String)

}