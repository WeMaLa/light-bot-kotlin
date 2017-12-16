package io.iconect.lightbot.domain.hap.service.characteristic

import org.assertj.core.api.Assertions
import org.junit.Test

class TargetTemperatureTest {

    @Test
    fun `verify predefined values`() {
        val targetTemperature = TargetTemperature(1)

        Assertions.assertThat(targetTemperature.instanceId).isEqualTo(1)
        Assertions.assertThat(targetTemperature.uuid).isEqualTo("00000035-0000-1000-8000-0026BB765291")
        Assertions.assertThat(targetTemperature.description).isNull()
        Assertions.assertThat(targetTemperature.value).isEqualTo("10.0")
        Assertions.assertThat(targetTemperature.perms).containsOnly(Permission.PAIRED_READ, Permission.PAIRED_WRITE, Permission.NOTIFY)
        Assertions.assertThat(targetTemperature.format).isEqualTo(Format.FLOAT)
        Assertions.assertThat(targetTemperature.unit).isEqualTo(Unit.CELSIUS)
        Assertions.assertThat(targetTemperature.minimumValue).isEqualTo(10.0)
        Assertions.assertThat(targetTemperature.maximumValue).isEqualTo(38.0)
        Assertions.assertThat(targetTemperature.stepValue).isNull()
        Assertions.assertThat(targetTemperature.maxLength).isNull()
        Assertions.assertThat(targetTemperature.maxDataLength).isNull()
    }

    @Test
    fun `verify custom values`() {
        val targetTemperature = TargetTemperature(2, "custom test description")

        Assertions.assertThat(targetTemperature.instanceId).isEqualTo(2)
        Assertions.assertThat(targetTemperature.description).isEqualTo("custom test description")
    }

    @Test
    fun `adjust value`() {
        val targetTemperature = TargetTemperature(3)
        targetTemperature.adjustValue(23.0)

        Assertions.assertThat(targetTemperature.instanceId).isEqualTo(3)
        Assertions.assertThat(targetTemperature.value).isEqualTo("23.0")

        targetTemperature.adjustValue(10.0)
        Assertions.assertThat(targetTemperature.value).isEqualTo("10.0")

        targetTemperature.adjustValue(38.0)
        Assertions.assertThat(targetTemperature.value).isEqualTo("38.0")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value to low`() {
        val targetTemperature = TargetTemperature(3)
        targetTemperature.adjustValue(9.9)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value to high`() {
        val targetTemperature = TargetTemperature(3)
        targetTemperature.adjustValue(38.1)
    }
}