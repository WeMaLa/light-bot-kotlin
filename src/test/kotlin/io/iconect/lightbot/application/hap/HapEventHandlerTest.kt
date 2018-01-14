package io.iconect.lightbot.application.hap

import io.iconect.lightbot.domain.hap.AccessoryFactory
import io.iconect.lightbot.domain.hap.AccessoryRepository
import io.iconect.lightbot.domain.hap.CharacteristicAdjustedEvent
import io.iconect.lightbot.domain.hap.CharacteristicAdjustedEventRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
class HapEventHandlerTest {

    @Autowired
    private lateinit var accessoryRepository : AccessoryRepository

    @Autowired
    private lateinit var accessoryFactory: AccessoryFactory

    @Autowired
    private lateinit var eventHandler: HapEventHandler

    @Autowired
    private lateinit var characteristicAdjustedEventRepository: CharacteristicAdjustedEventRepository

    @Before
    fun setUp() {
        accessoryRepository.clear()

        val thermostat = accessoryFactory.createThermostatAccessory(1, 11, 111, 112, 113, "Kitchen thermostat heater")
        accessoryRepository.store(thermostat)

        val window = accessoryFactory.createWindowAccessory(2, 21, 211, 212, 213, "Kitchen window")
        accessoryRepository.store(window)

        characteristicAdjustedEventRepository.clear()
    }

    @Test
    fun `verify current temperature is changed when receiving target temperature changed event`() {
        eventHandler.handleEvent(CharacteristicAdjustedEvent(1, 111, "19.3"))

        assertThat(accessoryRepository.findByInstanceId(1)?.findCharacteristic(112)?.value).isEqualTo("19.3")
        val event = characteristicAdjustedEventRepository.popNextEvent()
        assertThat(event?.accessoryId).isEqualTo(1)
        assertThat(event?.characteristicId).isEqualTo(112)
        assertThat(event?.value).isEqualTo("19.3")
    }

    @Test
    fun `verify current position is changed when receiving target position changed event`() {
        eventHandler.handleEvent(CharacteristicAdjustedEvent(2, 211, "10"))

        assertThat(accessoryRepository.findByInstanceId(2)?.findCharacteristic(212)?.value).isEqualTo("10")
        val event = characteristicAdjustedEventRepository.popNextEvent()
        assertThat(event?.accessoryId).isEqualTo(2)
        assertThat(event?.characteristicId).isEqualTo(212)
        assertThat(event?.value).isEqualTo("10")
    }
}