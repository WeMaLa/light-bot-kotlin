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

    private lateinit var existingAccessory: Accessory

    @Before
    fun setUp() {
        accessoryRepository = CachedAccessoryRepository()
        command = HapWritingCharacteristicsMessageCommand(accessoryRepository)

        val kitchenThermostat = Thermostat(2, 21, 22, 23)
        (kitchenThermostat.characteristics.first { c -> c is Name } as Name).updateName("Kitchen thermostat heater")
        existingAccessory = Accessory(1, listOf(kitchenThermostat))
        accessoryRepository.store(existingAccessory)
    }

    @Test
    fun `execute message`() {
        // TODO implement me
    }

    @Test
    fun `execute single write message and accessory not found`() {
        val content = buildSingleWriteCharacteristicsMessageContent(1000, 21, "19.0")

        val answer = command.executeMessage(content)
        assertThat(answer).isEqualTo("{\"characteristics\":[{\"aid\":1000,\"iid\":21,\"status\":-70409}]}")
    }

    @Test
    fun `execute multi write message and all accessories not found`() {
        val content = buildMultiWriteCharacteristicsMessageContent(1000, 21, "19.0", 1001, 21, "19.0")

        val answer = command.executeMessage(content)
        assertThat(answer).isEqualTo("{\"characteristics\":[{\"aid\":1000,\"iid\":21,\"status\":-70409},{\"aid\":1001,\"iid\":21,\"status\":-70409}]}")
    }

    companion object {

        fun buildSingleWriteCharacteristicsMessageContent(aid: Int, iid: Int, value: String): HapWritingCharacteristicsMessageContent {
            val writeCharacteristics = "{" +
                    "\"characteristics\" : [\n" +
                    "   {\n" +
                    "       \"aid\" : $aid,\n" +
                    "       \"iid\" : $iid,\n" +
                    "       \"value\" : $value\n" +
                    "   } " +
                    "]}"

            return buildWritingCharacteristicsMessageContent(writeCharacteristics)
        }

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