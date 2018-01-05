package io.iconect.lightbot.application.message

import io.iconect.lightbot.domain.hap.Accessory
import io.iconect.lightbot.domain.hap.AccessoryRepository
import io.iconect.lightbot.domain.hap.service.Thermostat
import io.iconect.lightbot.domain.hap.service.characteristic.Name
import io.iconect.lightbot.domain.message.content.HapWritingCharacteristicsMessageContent
import io.iconect.lightbot.infrastructure.CachedAccessoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class HapWritingCharacteristicsMessageCommandTest {

    private lateinit var command: HapWritingCharacteristicsMessageCommand

    private lateinit var accessoryRepository: AccessoryRepository

    @Before
    fun setUp() {
        accessoryRepository = CachedAccessoryRepository()
        command = HapWritingCharacteristicsMessageCommand(accessoryRepository)

        val kitchenThermostat = Thermostat(12, 121, 122, 123)
        (kitchenThermostat.characteristics.first { c -> c is Name } as Name).updateName("Kitchen thermostat heater")
        accessoryRepository.store(Accessory(1, listOf(kitchenThermostat)))

        val restRoomThermostat = Thermostat(22, 221, 222, 223)
        (restRoomThermostat.characteristics.first { c -> c is Name } as Name).updateName("Restroom thermostat heater")
        accessoryRepository.store(Accessory(2, listOf(restRoomThermostat)))
    }

    @Test
    fun `execute message and all write operations are successful`() {
        val content = buildMultiWriteCharacteristicsMessageContent(1, 121, "21.0", 2, 221, "19.5")

        val answer = command.executeMessage(content)

        assertThat(answer).isEqualTo("{\"characteristics\":[{\"aid\":1,\"iid\":121,\"value\":\"21.0\"},{\"aid\":2,\"iid\":221,\"value\":\"19.5\"}]}")
        // TODO assert repository
    }

    @Test
    fun `execute multi write message and all accessories not found`() {
        val content = buildMultiWriteCharacteristicsMessageContent(1000, 121, "19.0", 1001, 121, "19.0")

        val answer = command.executeMessage(content)
        assertThat(answer).isEqualTo("{\"characteristics\":[{\"aid\":1000,\"iid\":121,\"status\":-70409},{\"aid\":1001,\"iid\":121,\"status\":-70409}]}")
    }

    @Test
    fun `execute multi write message and one accessories not found and one characteristic not found`() {
        val content = buildMultiWriteCharacteristicsMessageContent(1, 2100, "19.0", 1000, 121, "19.0")

        val answer = command.executeMessage(content)
        assertThat(answer).isEqualTo("{\"characteristics\":[{\"aid\":1000,\"iid\":121,\"status\":-70409},{\"aid\":1,\"iid\":2100,\"status\":-70409}]}")
    }

    @Test
    fun `execute multi write message and one accessories not found and one characteristic is not writable`() {
        val content = buildMultiWriteCharacteristicsMessageContent(1000, 121, "19.0", 1, 122, "19.0")

        val answer = command.executeMessage(content)
        assertThat(answer).isEqualTo("{\"characteristics\":[{\"aid\":1000,\"iid\":121,\"status\":-70409},{\"aid\":1,\"iid\":122,\"status\":-70404}]}")
    }

    // TODO test a mix of success and failed

    companion object {

        fun buildMultiWriteCharacteristicsMessageContent(aid1: Int, iid1: Int, value1: String, aid2: Int, iid2: Int, value2: String): HapWritingCharacteristicsMessageContent {
            val writeCharacteristics = "{" +
                    "\"characteristics\" : [\n" +
                    "   {\n" +
                    "       \"aid\" : $aid1,\n" +
                    "       \"iid\" : $iid1,\n" +
                    "       \"value\" : $value1\n" +
                    "   }," +
                    "   {\n" +
                    "       \"aid\" : $aid2,\n" +
                    "       \"iid\" : $iid2,\n" +
                    "       \"value\" : $value2\n" +
                    "   } " +
                    "]}"

            return buildWritingCharacteristicsMessageContent(writeCharacteristics)
        }

        private fun buildWritingCharacteristicsMessageContent(singleWriteCharacteristics: String) =
                HapWritingCharacteristicsMessageContent(singleWriteCharacteristics, HapWritingCharacteristicsMessageContent.HapWritingCharacteristics.from(singleWriteCharacteristics))
    }
}