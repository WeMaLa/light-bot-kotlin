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
                .map { it.read() }
                .forEach {
                    val messageCommand = messageCommandFactory.createMessageCommand(it)
                    val messageAnswer = messageCommand.executeMessage(it)
                    log.info("Execute message '${it.message}' answers '$messageAnswer'")
                    // TODO test me
                    // TODO return answer to channel
                }
        log.info("${retrieveMessages.size} messages retrieved")
    }
}