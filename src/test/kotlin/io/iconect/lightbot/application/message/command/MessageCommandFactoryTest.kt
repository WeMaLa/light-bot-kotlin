package io.iconect.lightbot.application.message.command

import io.iconect.lightbot.domain.message.content.HapWritingCharacteristicsMessageContent
import io.iconect.lightbot.domain.message.content.UnknownMessageContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
class MessageCommandFactoryTest {

    @Autowired
    private lateinit var factory: MessageCommandFactory

    @Test
    fun `create writing characteristics message command`() {
        val writeCharacteristics = "{" +
                "\"characteristics\" : [\n" +
                "   {\n" +
                "       \"aid\" : 2,\n" +
                "       \"iid\" : 8,\n" +
                "       \"value\" : true\n" +
                "   } " +
                "]}"

        val writingCharacteristics = HapWritingCharacteristicsMessageContent.HapWritingCharacteristics.from(writeCharacteristics)

        val command = factory.createMessageCommand(HapWritingCharacteristicsMessageContent(writeCharacteristics, writingCharacteristics))

        assertThat(command::class).isEqualTo(HapWritingCharacteristicsMessageCommand::class)
    }

    @Test
    fun `create unknown message command`() {
        val command = factory.createMessageCommand(UnknownMessageContent("unknown-message"))

        assertThat(command::class).isEqualTo(UnknownMessageCommand::class)
    }
}