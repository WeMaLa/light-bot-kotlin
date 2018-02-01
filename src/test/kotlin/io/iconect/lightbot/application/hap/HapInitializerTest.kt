package io.iconect.lightbot.application.hap

import io.iconect.lightbot.domain.hap.AccessoryRepository
import io.iconect.lightbot.domain.hap.service.LightBulb
import io.iconect.lightbot.domain.hap.service.Thermostat
import io.iconect.lightbot.domain.hap.service.Window
import io.iconect.lightbot.domain.hap.service.characteristic.*
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
    fun `verify kitchen window initialization`() {
        hapInitializer.initialize()

        val accessory = accessoryRepository.findByInstanceId(11000)!!

        assertThat(accessory).isNotNull()
        assertThat(accessory.instanceId).isEqualTo(11000)
        assertThat(accessory.services.filter { s -> s is Window }.size).isEqualTo(1)

        val window = accessory.services.first { s -> s is Window } as Window
        assertThat(window.instanceId).isEqualTo(11100)

        val targetPosition = window.characteristics.first { c -> c is TargetPosition } as TargetPosition
        assertThat(targetPosition.instanceId).isEqualTo(11101)
        assertThat(targetPosition.accessoryInstanceId).isEqualTo(11000)
        assertThat(targetPosition.value).isEqualTo("0")

        val currentPosition = window.characteristics.first { c -> c is CurrentPosition } as CurrentPosition
        assertThat(currentPosition.instanceId).isEqualTo(11102)
        assertThat(currentPosition.accessoryInstanceId).isEqualTo(11000)
        assertThat(currentPosition.value).isEqualTo("0")

        val name = window.characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(11103)
        assertThat(name.accessoryInstanceId).isEqualTo(11000)
        assertThat(name.value).isEqualTo("Kitchen window")
    }

    @Test
    fun `verify kitchen light bulb initialization`() {
        hapInitializer.initialize()

        val accessory = accessoryRepository.findByInstanceId(12000)!!

        assertThat(accessory).isNotNull()
        assertThat(accessory.instanceId).isEqualTo(12000)
        assertThat(accessory.services.filter { s -> s is LightBulb }.size).isEqualTo(1)

        val lightbulb = accessory.services.first { s -> s is LightBulb } as LightBulb
        assertThat(lightbulb.instanceId).isEqualTo(12100)
        assertThat(lightbulb.accessoryInstanceId).isEqualTo(12000)

        val currentPosition = lightbulb.characteristics.first { c -> c is On } as On
        assertThat(currentPosition.instanceId).isEqualTo(12101)
        assertThat(currentPosition.accessoryInstanceId).isEqualTo(12000)
        assertThat(currentPosition.value).isEqualTo("off")

        val name = lightbulb.characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(12102)
        assertThat(name.accessoryInstanceId).isEqualTo(12000)
        assertThat(name.value).isEqualTo("Kitchen light bulb")
    }

    @Test
    fun `verify diner heater initialization`() {
        hapInitializer.initialize()

        val accessory = accessoryRepository.findByInstanceId(21000)!!

        assertThat(accessory).isNotNull()
        assertThat(accessory.instanceId).isEqualTo(21000)
        assertThat(accessory.services.filter { s -> s is Thermostat }.size).isEqualTo(1)

        val thermostat = accessory.services.first { s -> s is Thermostat } as Thermostat
        assertThat(thermostat.instanceId).isEqualTo(21100)

        val targetTemperature = thermostat.characteristics.first { c -> c is TargetTemperature } as TargetTemperature
        assertThat(targetTemperature.instanceId).isEqualTo(21101)
        assertThat(targetTemperature.accessoryInstanceId).isEqualTo(21000)
        assertThat(targetTemperature.value).isEqualTo("10.0")

        val currentTemperature = thermostat.characteristics.first { c -> c is CurrentTemperature } as CurrentTemperature
        assertThat(currentTemperature.instanceId).isEqualTo(21102)
        assertThat(currentTemperature.accessoryInstanceId).isEqualTo(21000)
        assertThat(currentTemperature.value).isEqualTo("0.0")

        val name = thermostat.characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(21103)
        assertThat(name.accessoryInstanceId).isEqualTo(21000)
        assertThat(name.value).isEqualTo("Diner thermostat heater")
    }
}