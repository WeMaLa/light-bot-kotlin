package io.iconect.lightbot.infrastructure.message

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduleMessages @Autowired constructor(private var serverMessageExchangeService: ServerMessageExchangeService){

    private val log = LoggerFactory.getLogger(ScheduleMessages::class.java)

    @Scheduled(fixedRate = 3000)
    fun handleUnreadMessages() {
        log.info("Start retrieving latest messages")
        val retrieveMessages = serverMessageExchangeService.retrieveMessages()
        log.info("${retrieveMessages.size} messages retrieved")
    }
}