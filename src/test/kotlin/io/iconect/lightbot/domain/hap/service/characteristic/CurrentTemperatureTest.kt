package io.iconect.lightbot.domain.hap.service.characteristic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CurrentTemperatureTest {

    @Test
    fun `verify predefined values`() {
        val currentTemperature = CurrentTemperature(1)

        assertThat(currentTemperature.instanceId).isEqualTo(1)
        assertThat(currentTemperature.uuid).isEqualTo("00000011-0000-1000-8000-0026BB765291")
        assertThat(currentTemperature.type).isEqualTo("public.hap.characteristic.temperature.current")
        assertThat(currentTemperature.description).isNull()
        assertThat(currentTemperature.value).isEqualTo("0.0")
        assertThat(currentTemperature.permissions).containsOnly(Permission.PAIRED_READ, Permission.NOTIFY)
        assertThat(currentTemperature.format).isEqualTo(Format.FLOAT)
        assertThat(currentTemperature.unit).isEqualTo(Unit.CELSIUS)
        assertThat(currentTemperature.minimumValue).isEqualTo(0.0)
        assertThat(currentTemperature.maximumValue).isEqualTo(100.0)
        assertThat(currentTemperature.stepValue).isEqualTo(0.1)
        assertThat(currentTemperature.maximumLength).isNull()
        assertThat(currentTemperature.maxDataLength).isNull()
    }

    @Test
    fun `adjust value`() {
        val currentTemperature = CurrentTemperature(3)
        currentTemperature.adjustValue(23.0)

        assertThat(currentTemperature.instanceId).isEqualTo(3)
        assertThat(currentTemperature.value).isEqualTo("23.0")

        currentTemperature.adjustValue(0.0)
        assertThat(currentTemperature.value).isEqualTo("0.0")

        currentTemperature.adjustValue(100.0)
        assertThat(currentTemperature.value).isEqualTo("100.0")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value to low`() {
        val targetTemperature = CurrentTemperature(3)
        targetTemperature.adjustValue(-0.1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value to high`() {
        val targetTemperature = CurrentTemperature(3)
        targetTemperature.adjustValue(100.1)
    }
}