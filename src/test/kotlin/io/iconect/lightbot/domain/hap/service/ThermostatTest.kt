package io.iconect.lightbot.domain.hap.service

import io.iconect.lightbot.domain.hap.service.characteristic.CurrentTemperature
import io.iconect.lightbot.domain.hap.service.characteristic.Name
import io.iconect.lightbot.domain.hap.service.characteristic.TargetTemperature
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ThermostatTest {

    @Test
    fun `verify predefined values`() {
        val thermostat = Thermostat(1, 2, 3, 4, 5)

        assertThat(thermostat.instanceId).isEqualTo(1)
        assertThat(thermostat.accessoryInstanceId).isEqualTo(2)
        assertThat(thermostat.uuid).isEqualTo("0000004A-0000-1000-8000-0026BB765291")
        assertThat(thermostat.type).isEqualTo("public.hap.service.thermostat")
        assertThat(thermostat.hidden).isFalse()
        assertThat(thermostat.primaryService).isTrue()
        assertThat(thermostat.linkedServices).isEmpty()
        assertThat(thermostat.characteristics.filter { c -> c is TargetTemperature }.size).isEqualTo(1)
        assertThat(thermostat.characteristics.filter { c -> c is CurrentTemperature }.size).isEqualTo(1)
        assertThat(thermostat.characteristics.filter { c -> c is Name }.size).isEqualTo(1)

        val targetTemperature = thermostat.characteristics.first { c -> c is TargetTemperature } as TargetTemperature
        assertThat(targetTemperature.instanceId).isEqualTo(3)

        val currentTemperature = thermostat.characteristics.first { c -> c is CurrentTemperature } as CurrentTemperature
        assertThat(currentTemperature.instanceId).isEqualTo(4)

        val name = thermostat.characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(5)
    }

}