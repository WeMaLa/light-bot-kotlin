package io.iconect.lightbot.domain.hap.service.characteristic

import org.assertj.core.api.Assertions
import org.junit.Test

class CurrentTemperatureTest {

    @Test
    fun `verify predefined values`() {
        val currentTemperature = CurrentTemperature(1)

        Assertions.assertThat(currentTemperature.instanceId).isEqualTo(1)
        Assertions.assertThat(currentTemperature.uuid).isEqualTo("00000011-0000-1000-8000-0026BB765291")
        Assertions.assertThat(currentTemperature.type).isEqualTo("public.hap.characteristic.temperature.current")
        Assertions.assertThat(currentTemperature.description).isNull()
        Assertions.assertThat(currentTemperature.value).isEqualTo("0.0")
        Assertions.assertThat(currentTemperature.permissions).containsOnly(Permission.PAIRED_READ, Permission.NOTIFY)
        Assertions.assertThat(currentTemperature.format).isEqualTo(Format.FLOAT)
        Assertions.assertThat(currentTemperature.unit).isEqualTo(Unit.CELSIUS)
        Assertions.assertThat(currentTemperature.minimumValue).isEqualTo(0.0)
        Assertions.assertThat(currentTemperature.maximumValue).isEqualTo(100.0)
        Assertions.assertThat(currentTemperature.stepValue).isEqualTo(0.1)
        Assertions.assertThat(currentTemperature.maximumLength).isNull()
        Assertions.assertThat(currentTemperature.maxDataLength).isNull()
    }

    @Test
    fun `adjust value`() {
        val currentTemperature = CurrentTemperature(3)
        currentTemperature.adjustValue(23.0)

        Assertions.assertThat(currentTemperature.instanceId).isEqualTo(3)
        Assertions.assertThat(currentTemperature.value).isEqualTo("23.0")

        currentTemperature.adjustValue(0.0)
        Assertions.assertThat(currentTemperature.value).isEqualTo("0.0")

        currentTemperature.adjustValue(100.0)
        Assertions.assertThat(currentTemperature.value).isEqualTo("100.0")
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