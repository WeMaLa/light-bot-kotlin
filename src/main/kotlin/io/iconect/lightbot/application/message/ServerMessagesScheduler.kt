package io.iconect.lightbot.application.message

import io.iconect.lightbot.domain.message.ServerMessageRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ServerMessagesScheduler @Autowired constructor(private var serverMessageRepository: ServerMessageRepository){

    private val log = LoggerFactory.getLogger(ServerMessagesScheduler::class.java)

    @Scheduled(fixedRate = 3000)
    fun handleUnreadMessages() {
        log.info("Start retrieving latest messages")
        val retrieveMessages = serverMessageRepository.retrieveMessages()
        log.info("${retrieveMessages.size} messages retrieved")
    }
}