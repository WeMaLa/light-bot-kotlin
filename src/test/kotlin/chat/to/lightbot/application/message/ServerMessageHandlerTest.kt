package chat.to.lightbot.application.message

import chat.to.lightbot.domain.hap.Accessory
import chat.to.lightbot.domain.hap.AccessoryRepository
import chat.to.lightbot.domain.hap.service.Thermostat
import chat.to.lightbot.domain.hap.service.characteristic.Name
import chat.to.lightbot.domain.message.ServerMessageFactory
import chat.to.lightbot.domain.message.ServerMessageRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import com.nhaarman.mockitokotlin2.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner


@SpringBootTest
@ActiveProfiles("unittest")
class ServerMessageHandlerTest {

    @Autowired
    private lateinit var serverMessageHandler: ServerMessageHandler

    @Autowired
    private lateinit var serverMessageFactory: ServerMessageFactory

    @Autowired
    private lateinit var accessoryRepository: AccessoryRepository

    @MockBean
    private lateinit var serverMessageRepositoryMock: ServerMessageRepository

    @BeforeEach
    fun setUp() {
        val kitchenThermostat = Thermostat(12, 120, 121, 122, 123, { _, _, _ -> })
        (kitchenThermostat.characteristics.first { c -> c is Name } as Name).updateName("Kitchen thermostat heater")
        accessoryRepository.store(Accessory(1, listOf(kitchenThermostat)))

        val restRoomThermostat = Thermostat(22, 220, 221, 222, 223, { _, _, _ -> })
        (restRoomThermostat.characteristics.first { c -> c is Name } as Name).updateName("Restroom thermostat heater")
        accessoryRepository.store(Accessory(2, listOf(restRoomThermostat)))
    }

    @Test
    fun `handle message with type UNKNOWN`() {
        val serverMessage = serverMessageFactory.createServerMessage("unit-test-message-1", "unit-test-channel")

        serverMessageHandler.handleMessage(serverMessage)

        verify(serverMessageRepositoryMock).sendMessage("unit-test-channel", "I do not understand the command '${serverMessage.raw}'")
    }

    @Test
    fun `handle HAP_WRITING_CHARACTERISTICS message and all write operations are successful`() {
        val message = ServerMessageHandlerTest.buildMultiWriteCharacteristicsMessageContent(1, 121, "21.0", 2, 221, "19.5")
        val serverMessage = serverMessageFactory.createServerMessage(message, "unit-test-channel")

        serverMessageHandler.handleMessage(serverMessage)

        verify(serverMessageRepositoryMock).sendMessage("unit-test-channel", "{\"characteristics\":[{\"aid\":1,\"iid\":121,\"value\":\"21.0\"},{\"aid\":2,\"iid\":221,\"value\":\"19.5\"}]}")
        assertThat(accessoryRepository.findByInstanceId(1)!!.findCharacteristic(121)!!.value).isEqualTo("21.0")
        assertThat(accessoryRepository.findByInstanceId(2)!!.findCharacteristic(221)!!.value).isEqualTo("19.5")
    }

    @Test
    fun `handle HAP_WRITING_CHARACTERISTICS message and one accessory is not found but the other is successful`() {
        val message = ServerMessageHandlerTest.buildMultiWriteCharacteristicsMessageContent(1, 121, "21.0", 2000, 221, "19.5")
        val serverMessage = serverMessageFactory.createServerMessage(message, "unit-test-channel")

        serverMessageHandler.handleMessage(serverMessage)

        verify(serverMessageRepositoryMock).sendMessage("unit-test-channel", "{\"characteristics\":[{\"aid\":1,\"iid\":121,\"status\":0},{\"aid\":2000,\"iid\":221,\"status\":-70409}]}")
        assertThat(accessoryRepository.findByInstanceId(1)!!.findCharacteristic(121)!!.value).isEqualTo("21.0")
        assertThat(accessoryRepository.findByInstanceId(2)!!.findCharacteristic(221)!!.value).isEqualTo("10.0")
    }

    @Test
    fun `handle HAP_WRITING_CHARACTERISTICS message and all accessories not found`() {
        val message = ServerMessageHandlerTest.buildMultiWriteCharacteristicsMessageContent(1000, 121, "19.0", 1001, 121, "19.0")
        val serverMessage = serverMessageFactory.createServerMessage(message, "unit-test-channel")

        serverMessageHandler.handleMessage(serverMessage)

        verify(serverMessageRepositoryMock).sendMessage("unit-test-channel", "{\"characteristics\":[{\"aid\":1000,\"iid\":121,\"status\":-70409},{\"aid\":1001,\"iid\":121,\"status\":-70409}]}")
        assertThat(accessoryRepository.findByInstanceId(1)!!.findCharacteristic(121)!!.value).isEqualTo("10.0")
        assertThat(accessoryRepository.findByInstanceId(2)!!.findCharacteristic(221)!!.value).isEqualTo("10.0")
    }

    @Test
    fun `handle HAP_WRITING_CHARACTERISTICS message and one accessories not found and one characteristic not found`() {
        val message = ServerMessageHandlerTest.buildMultiWriteCharacteristicsMessageContent(1, 2100, "19.0", 1000, 121, "19.0")
        val serverMessage = serverMessageFactory.createServerMessage(message, "unit-test-channel")

        serverMessageHandler.handleMessage(serverMessage)

        verify(serverMessageRepositoryMock).sendMessage("unit-test-channel", "{\"characteristics\":[{\"aid\":1000,\"iid\":121,\"status\":-70409},{\"aid\":1,\"iid\":2100,\"status\":-70409}]}")
        assertThat(accessoryRepository.findByInstanceId(1)!!.findCharacteristic(121)!!.value).isEqualTo("10.0")
        assertThat(accessoryRepository.findByInstanceId(2)!!.findCharacteristic(221)!!.value).isEqualTo("10.0")
    }

    @Test
    fun `handle HAP_WRITING_CHARACTERISTICS message and one accessories not found and one characteristic is not writable`() {
        val message = ServerMessageHandlerTest.buildMultiWriteCharacteristicsMessageContent(1000, 121, "19.0", 1, 122, "19.0")
        val serverMessage = serverMessageFactory.createServerMessage(message, "unit-test-channel")

        serverMessageHandler.handleMessage(serverMessage)

        verify(serverMessageRepositoryMock).sendMessage("unit-test-channel", "{\"characteristics\":[{\"aid\":1000,\"iid\":121,\"status\":-70409},{\"aid\":1,\"iid\":122,\"status\":-70404}]}")
        assertThat(accessoryRepository.findByInstanceId(1)!!.findCharacteristic(121)!!.value).isEqualTo("10.0")
        assertThat(accessoryRepository.findByInstanceId(2)!!.findCharacteristic(221)!!.value).isEqualTo("10.0")
    }

    companion object {

        fun buildMultiWriteCharacteristicsMessageContent(aid1: Int, iid1: Int, value1: String, aid2: Int, iid2: Int, value2: String): String {
            return "{" +
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
        }
    }
}