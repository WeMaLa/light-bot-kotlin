package io.iconect.lightbot.application.message.command

import io.iconect.lightbot.domain.hap.AccessoryRepository
import io.iconect.lightbot.domain.message.content.HapWritingCharacteristicsMessageContent
import io.iconect.lightbot.domain.message.content.MessageContent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MessageCommandFactory @Autowired constructor(private var accessoryRepository: AccessoryRepository) {

    fun createMessageCommand(messageContent: MessageContent<Any>): MessageCommand<*> {
        if (messageContent is HapWritingCharacteristicsMessageContent) {
            return HapWritingCharacteristicsMessageCommand(accessoryRepository)
        }

        return UnknownMessageCommand()
    }

}