package io.iconect.lightbot.infrastructure.message

import io.iconect.lightbot.application.message.ServerMessageHandler
import io.iconect.lightbot.domain.message.ServerMessageRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ServerMessageScheduler @Autowired constructor(
        private var serverMessageRepository: ServerMessageRepository,
        private val serverMessageHandler: ServerMessageHandler) {

    private val log = LoggerFactory.getLogger(ServerMessageScheduler::class.java)

    @Scheduled(fixedRate = 3000)
    fun scheduleUnreadMessages() {
        log.info("Start retrieving latest messages")
        val retrieveMessages = serverMessageRepository.retrieveMessages()
        retrieveMessages.forEach { serverMessageHandler.handleMessage(it) }
        log.info("${retrieveMessages.size} messages retrieved")
    }

}