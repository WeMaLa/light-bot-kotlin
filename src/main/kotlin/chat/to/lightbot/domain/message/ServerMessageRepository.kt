package chat.to.lightbot.domain.message

interface ServerMessageRepository {

    fun retrieveMessages(): List<ServerMessage>

    fun sendMessage(channelIdentifier: String, message: String)

}