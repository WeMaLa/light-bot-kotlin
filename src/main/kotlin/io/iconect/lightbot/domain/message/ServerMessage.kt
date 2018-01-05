package io.iconect.lightbot.domain.message

import io.iconect.lightbot.domain.message.content.HapWritingCharacteristicsMessageContent
import io.iconect.lightbot.domain.message.content.MessageContent
import io.iconect.lightbot.domain.message.content.UnknownMessageContent
import io.iconect.lightbot.domain.message.content.exception.JsonConvertException

data class ServerMessage constructor(val content: String, val channel: String) {

    fun read() : MessageContent<Any> {
        try {
            val hapWritingCharacteristics = HapWritingCharacteristicsMessageContent.HapWritingCharacteristics.from(content)
            return HapWritingCharacteristicsMessageContent(content, hapWritingCharacteristics)
        } catch (e: JsonConvertException) {
            // do nothing. Message is not hap writing characteristics
        }

        return UnknownMessageContent(content)
    }

}