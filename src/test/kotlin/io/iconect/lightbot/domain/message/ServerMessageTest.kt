package io.iconect.lightbot.domain.message

import io.iconect.lightbot.domain.message.content.HapWritingCharacteristicsMessageContent
import io.iconect.lightbot.domain.message.content.UnknownMessageContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ServerMessageTest {

    @Test
    fun `read hap writing characteristics message`() {
        val singleWriteCharacteristics = "{" +
                "\"characteristics\" : [\n" +
                "   {\n" +
                "       \"aid\" : 2,\n" +
                "       \"iid\" : 8,\n" +
                "       \"value\" : true\n" +
                "   } " +
                "]}"

        val content = ServerMessage(singleWriteCharacteristics, "unit-test-message-channel").read()

        assertThat(content::class).isEqualTo(HapWritingCharacteristicsMessageContent::class)

        content as HapWritingCharacteristicsMessageContent

        assertThat(content.message).isEqualTo(singleWriteCharacteristics)
        assertThat(content.content.characteristics.size).isEqualTo(1)
        assertThat(content.content.characteristics[0].aid).isEqualTo(2)
        assertThat(content.content.characteristics[0].iid).isEqualTo(8)
        assertThat(content.content.characteristics[0].value).isEqualTo("true")
    }

    @Test
    fun `read unmappable server message`() {
        val content = ServerMessage("unit-test-unknown", "unit-test-message-channel").read()

        assertThat(content::class).isEqualTo(UnknownMessageContent::class)

        content as UnknownMessageContent

        assertThat(content.message).isEqualTo("unit-test-unknown")
        assertThat(content.content).isEqualTo(Unit)
    }
}