package io.iconect.lightbot.application.message.command

interface MessageCommand<in T: Any> {

    /**
     * Executes message content.
     *
     * @param content the message content
     * @return the text message answer to send to iconect server
     */
    fun executeMessage(content: T): String

}