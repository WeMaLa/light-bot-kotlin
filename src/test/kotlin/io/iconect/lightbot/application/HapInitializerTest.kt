package io.iconect.lightbot.application

import io.iconect.lightbot.domain.hap.service.Thermostat
import io.iconect.lightbot.domain.hap.service.characteristic.CurrentTemperature
import io.iconect.lightbot.domain.hap.service.characteristic.TargetTemperature
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class HapInitializerTest {

    @Test
    fun `verify initialization`() {
        val accessory = HapInitializer.initialize()

        assertThat(accessory).isNotNull()
        assertThat(accessory.instanceId).isEqualTo(1)
        assertThat(accessory.services.filter { s -> s is Thermostat }.size).isEqualTo(1)

        val thermostat = accessory.services.first { s -> s is Thermostat } as Thermostat
        assertThat(thermostat.instanceId).isEqualTo(2)
        assertThat(thermostat.uuid).isEqualTo("0000004A-0000-1000-8000-0026BB765291")
        assertThat(thermostat.hidden).isFalse()
        assertThat(thermostat.linkedServices).isEmpty()
        assertThat(thermostat.primaryService).isTrue()
        assertThat(thermostat.type).isEqualTo("public.hap.service.thermostat")
        assertThat(thermostat.characteristics.filter { c -> c is TargetTemperature }.size).isEqualTo(1)
        assertThat(thermostat.characteristics.filter { c -> c is CurrentTemperature }.size).isEqualTo(1)

        val targetTemperature = thermostat.characteristics.first { c -> c is TargetTemperature } as TargetTemperature
        assertThat(targetTemperature.instanceId).isEqualTo(21)
        assertThat(targetTemperature.description).isEqualTo("Kitchen heater")
        assertThat(targetTemperature.value).isEqualTo("10.0")

        val currentTemperature = thermostat.characteristics.first { c -> c is CurrentTemperature } as CurrentTemperature
        assertThat(currentTemperature.instanceId).isEqualTo(22)
        assertThat(currentTemperature.description).isEqualTo("Kitchen heater")
        assertThat(currentTemperature.value).isEqualTo("0.0")
    }
}