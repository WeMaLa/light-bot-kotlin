package chat.to.lightbot.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import chat.to.lightbot.application.hap.HapEventHandler
import chat.to.lightbot.domain.hap.CharacteristicAdjustedEventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CharacteristicAdjustedEventScheduler @Autowired constructor(
        private val messagingTemplate: SimpMessagingTemplate,
        private val characteristicAdjustedEventRepository: CharacteristicAdjustedEventRepository,
        private val hapEventHandler: HapEventHandler) {

    @Scheduled(fixedRate = 60)
    fun scheduleEvents() {
        val event = characteristicAdjustedEventRepository.popNextEvent()

        if (event != null) {
            val eventAsJson = ObjectMapper().writeValueAsString(event)
            messagingTemplate.convertAndSend("/topic/event", eventAsJson)
            hapEventHandler.handleEvent(event)
        }
    }

}