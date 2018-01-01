package io.iconect.lightbot.application

import io.iconect.lightbot.domain.AccessoryRepository
import io.iconect.lightbot.domain.service.Thermostat
import io.iconect.lightbot.domain.service.characteristic.CurrentTemperature
import io.iconect.lightbot.domain.service.characteristic.Name
import io.iconect.lightbot.domain.service.characteristic.TargetTemperature
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
class HapInitializerTest {

    @Autowired
    private lateinit var accessoryRepository: AccessoryRepository

    @Autowired
    private lateinit var hapInitializer: HapInitializer

    @Test
    fun `verify initialization`() {
        hapInitializer.initialize()

        val accessories = accessoryRepository.findAll()

        assertThat(accessories.size).isEqualTo(1)

        val accessory = accessories[0]

        assertThat(accessory).isNotNull()
        assertThat(accessory.instanceId).isEqualTo(1)
        assertThat(accessory.services.filter { s -> s is Thermostat }.size).isEqualTo(1)

        val thermostat = accessory.services.first { s -> s is Thermostat } as Thermostat
        assertThat(thermostat.instanceId).isEqualTo(2)

        val targetTemperature = thermostat.characteristics.first { c -> c is TargetTemperature } as TargetTemperature
        assertThat(targetTemperature.instanceId).isEqualTo(21)
        assertThat(targetTemperature.value).isEqualTo("10.0")

        val currentTemperature = thermostat.characteristics.first { c -> c is CurrentTemperature } as CurrentTemperature
        assertThat(currentTemperature.instanceId).isEqualTo(22)
        assertThat(currentTemperature.value).isEqualTo("0.0")

        val name = thermostat.characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(23)
        assertThat(name.value).isEqualTo("Kitchen thermostat heater")
    }
}