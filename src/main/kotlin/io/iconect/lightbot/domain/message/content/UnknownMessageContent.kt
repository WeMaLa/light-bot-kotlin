package io.iconect.lightbot.domain.message.content

class UnknownMessageContent(override val message: String) : MessageContent<Unit> {
    override val content: Unit
        get() = Unit
}