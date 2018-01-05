package io.iconect.lightbot.application.message

import io.iconect.lightbot.domain.message.content.MessageContent

interface MessageCommand<in T: MessageContent<Any>> {

    /**
     * Executes message content.
     *
     * @param content the message content
     * @return the text message answer to send to iconect server
     */
    fun executeMessage(content: T): String

}