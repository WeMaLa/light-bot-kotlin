package io.iconect.lightbot.application.message

import io.iconect.lightbot.application.message.command.MessageCommandFactory
import io.iconect.lightbot.domain.message.ServerMessageRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ServerMessagesScheduler @Autowired constructor(
        private var serverMessageRepository: ServerMessageRepository,
        private var messageCommandFactory: MessageCommandFactory){

    private val log = LoggerFactory.getLogger(ServerMessagesScheduler::class.java)

    @Scheduled(fixedRate = 3000)
    fun handleUnreadMessages() {
        log.info("Start retrieving latest messages")
        val retrieveMessages = serverMessageRepository.retrieveMessages()
        retrieveMessages
                .forEach {
                    val messageContent = it.read()
                    val messageCommand = messageCommandFactory.createMessageCommand(messageContent)
                    val messageAnswer = messageCommand.executeMessage(messageContent)
                    log.info("Execute message '${messageContent.message}' answers '$messageAnswer'")
                    serverMessageRepository.sendMessage(it.channel, messageAnswer)
                }
        log.info("${retrieveMessages.size} messages retrieved")
    }
}