package io.iconect.lightbot.domain.message

import org.springframework.stereotype.Component

@Component
class ServerMessageFactory {

    fun createServerMessage(message: String, channel: String): ServerMessage {
        try {
            val hapWritingCharacteristics = ServerMessage.HapWritingCharacteristics.from(message)
            return ServerMessage(message, channel, ServerMessageType.HAP_WRITING_CHARACTERISTICS, hapWritingCharacteristics)
        } catch (e: ServerMessage.JsonConvertException) {
            // do nothing. Message is not hap writing characteristics
        }

        return ServerMessage(message, channel)
    }

}