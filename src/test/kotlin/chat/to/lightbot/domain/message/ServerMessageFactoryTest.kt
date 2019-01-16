package chat.to.lightbot.domain.message

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.Test

class ServerMessageFactoryTest {

    private val serverMessageFactory: ServerMessageFactory = ServerMessageFactory()

    @Test
    fun `create hap writing characteristics server message with a single characteristics`() {
        val characteristics = "{" +
                "\"characteristics\" : [\n" +
                "   {\n" +
                "       \"aid\" : 2,\n" +
                "       \"iid\" : 8,\n" +
                "       \"value\" : true\n" +
                "   } " +
                "]}"

        val serverMessage = serverMessageFactory.createServerMessage(characteristics, "unit-test-channel")

        assertThat(serverMessage.raw).isEqualTo(characteristics)
        assertThat(serverMessage.channel).isEqualTo("unit-test-channel")
        assertThat(serverMessage.type).isEqualTo(ServerMessageType.HAP_WRITING_CHARACTERISTICS)

        val content = serverMessage.content as ServerMessage.HapWritingCharacteristics

        assertThat(content.characteristics.size).isEqualTo(1)
        assertThat(content.characteristics[0].aid).isEqualTo(2)
        assertThat(content.characteristics[0].iid).isEqualTo(8)
        assertThat(content.characteristics[0].value).isEqualTo("true")
    }

    @Test
    fun `create hap writing characteristics server message with a multi characteristics`() {
        val characteristics = "{" +
                "\"characteristics\" : [\n" +
                "   {\n" +
                "       \"aid\" : 2,\n" +
                "       \"iid\" : 8,\n" +
                "       \"value\" : true\n" +
                "   }," +
                "   {\n" +
                "       \"aid\" : 3,\n" +
                "       \"iid\" : 9,\n" +
                "       \"value\" : \"on\"\n" +
                "   } " +
                "]}"

        val serverMessage = serverMessageFactory.createServerMessage(characteristics, "unit-test-channel")

        assertThat(serverMessage.raw).isEqualTo(characteristics)
        assertThat(serverMessage.channel).isEqualTo("unit-test-channel")
        assertThat(serverMessage.type).isEqualTo(ServerMessageType.HAP_WRITING_CHARACTERISTICS)

        val content = serverMessage.content as ServerMessage.HapWritingCharacteristics

        assertThat(content.characteristics.size).isEqualTo(2)
        assertThat(content.characteristics)
                .extracting("aid", "iid", "value")
                .containsOnly(
                        tuple(2, 8, "true"),
                        tuple(3, 9, "on")
                )
    }

    @Test
    fun `create broken hap writing characteristics server message`() {
        val incomplete = "{" +
                "\"characteristics\" : [\n" +
                "   {\n" +
                "       \"aid\" : 2,\n" +
                "       \"iid\" : 8\n" +
                "   } " +
                "]}"

        val serverMessage = serverMessageFactory.createServerMessage(incomplete, "unit-test-channel")

        assertThat(serverMessage.raw).isEqualTo(incomplete)
        assertThat(serverMessage.channel).isEqualTo("unit-test-channel")
        assertThat(serverMessage.type).isEqualTo(ServerMessageType.UNKNOWN)
        assertThat(serverMessage.content).isEqualTo(Unit)
    }

    @Test
    fun `create not mappable server message`() {
        val serverMessage = serverMessageFactory.createServerMessage("unit-test-unknown", "unit-test-channel")

        assertThat(serverMessage.raw).isEqualTo("unit-test-unknown")
        assertThat(serverMessage.channel).isEqualTo("unit-test-channel")
        assertThat(serverMessage.type).isEqualTo(ServerMessageType.UNKNOWN)
        assertThat(serverMessage.content).isEqualTo(Unit)
    }

    @Test
    fun `create empty server message`() {
        val serverMessage = serverMessageFactory.createServerMessage("", "unit-test-channel")

        assertThat(serverMessage.raw).isEqualTo("")
        assertThat(serverMessage.channel).isEqualTo("unit-test-channel")
        assertThat(serverMessage.type).isEqualTo(ServerMessageType.UNKNOWN)
        assertThat(serverMessage.content).isEqualTo(Unit)
    }

    @Test
    fun `create blank server message`() {
        val serverMessage = serverMessageFactory.createServerMessage("   ", "unit-test-channel")

        assertThat(serverMessage.raw).isEqualTo("   ")
        assertThat(serverMessage.channel).isEqualTo("unit-test-channel")
        assertThat(serverMessage.type).isEqualTo(ServerMessageType.UNKNOWN)
        assertThat(serverMessage.content).isEqualTo(Unit)
    }

}