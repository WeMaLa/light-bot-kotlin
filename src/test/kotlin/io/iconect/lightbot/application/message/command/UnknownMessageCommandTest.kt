package io.iconect.lightbot.application.message.command

import io.iconect.lightbot.application.message.command.UnknownMessageCommand
import io.iconect.lightbot.domain.message.content.UnknownMessageContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UnknownMessageCommandTest {

    @Test
    fun `execute message`() {
        val answer = UnknownMessageCommand().executeMessage(UnknownMessageContent("unit-test-message"))

        assertThat(answer).isEqualTo("I do not understand the command 'unit-test-message'")
    }
}