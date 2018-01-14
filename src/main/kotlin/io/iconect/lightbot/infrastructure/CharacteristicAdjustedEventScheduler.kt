package io.iconect.lightbot.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import io.iconect.lightbot.domain.hap.CharacteristicAdjustedEventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CharacteristicAdjustedEventScheduler @Autowired constructor(
        private val messagingTemplate: SimpMessagingTemplate,
        private val characteristicAdjustedEventRepository: CharacteristicAdjustedEventRepository) {

    @Scheduled(fixedRate = 500)
    fun scheduleEvents() {
        val event = characteristicAdjustedEventRepository.popNextEvent()

        if (event != null) {
            val eventAsJson = ObjectMapper().writeValueAsString(event)
            messagingTemplate.convertAndSend("/topic/event", eventAsJson)
        }
    }

}