package chat.to.lightbot.infrastructure.message

import chat.to.lightbot.domain.message.ServerMessage
import chat.to.lightbot.domain.message.ServerMessageFactory
import chat.to.lightbot.domain.message.ServerMessageRepository
import chat.to.server.bot.message.ServerMessageExchangeService
import org.springframework.stereotype.Repository

@Repository
class ServerMessageRepositoryImpl(private var serverMessageExchangeService: ServerMessageExchangeService,
                                  private var serverMessageFactory: ServerMessageFactory) : ServerMessageRepository {
    override fun retrieveMessages(): List<ServerMessage> {
        return serverMessageExchangeService.retrieveMessages().map { serverMessageFactory.createServerMessage(it.text, it._links.channel.href.replace("/api/channel/", "")) }
    }

    override fun sendMessage(channelIdentifier: String, message: String) {
        serverMessageExchangeService.sendMessage(channelIdentifier, message)
    }

}