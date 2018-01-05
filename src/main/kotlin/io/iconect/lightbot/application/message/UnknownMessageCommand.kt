package io.iconect.lightbot.application.message

import io.iconect.lightbot.domain.message.content.UnknownMessageContent

class UnknownMessageCommand : MessageCommand<UnknownMessageContent> {

    override fun executeMessage(content: UnknownMessageContent) : String {
        return "I do not understand the command '${content.message}'"
    }
}