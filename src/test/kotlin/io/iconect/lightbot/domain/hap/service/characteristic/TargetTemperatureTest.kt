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
        Assertions.assertThat(targetTemperature.value).isEqualTo("10.0F")
        Assertions.assertThat(targetTemperature.perms).containsOnly(Permission.PAIRED_READ, Permission.PAIRED_WRITE, Permission.NOTIFY)
        Assertions.assertThat(targetTemperature.format).isEqualTo(Format.FLOAT)
        Assertions.assertThat(targetTemperature.unit).isEqualTo(Unit.CELSIUS)
        Assertions.assertThat(targetTemperature.minimumValue).isEqualTo(10.0F)
        Assertions.assertThat(targetTemperature.maximumValue).isEqualTo(38.0F)
        Assertions.assertThat(targetTemperature.stepValue).isNull()
        Assertions.assertThat(targetTemperature.maxLength).isNull()
        Assertions.assertThat(targetTemperature.maxDataLength).isNull()
    }

    @Test
    fun `verify custom values`() {
        val targetTemperature = TargetTemperature(2, "20.0F", "custom test description")

        Assertions.assertThat(targetTemperature.instanceId).isEqualTo(2)
        Assertions.assertThat(targetTemperature.uuid).isEqualTo("00000035-0000-1000-8000-0026BB765291")
        Assertions.assertThat(targetTemperature.description).isEqualTo("custom test description")
        Assertions.assertThat(targetTemperature.value).isEqualTo("20.0F")
        Assertions.assertThat(targetTemperature.perms).containsOnly(Permission.PAIRED_READ, Permission.PAIRED_WRITE, Permission.NOTIFY)
        Assertions.assertThat(targetTemperature.format).isEqualTo(Format.FLOAT)
        Assertions.assertThat(targetTemperature.unit).isEqualTo(Unit.CELSIUS)
        Assertions.assertThat(targetTemperature.minimumValue).isEqualTo(10.0F)
        Assertions.assertThat(targetTemperature.maximumValue).isEqualTo(38.0F)
        Assertions.assertThat(targetTemperature.stepValue).isNull()
        Assertions.assertThat(targetTemperature.maxLength).isNull()
        Assertions.assertThat(targetTemperature.maxDataLength).isNull()
    }
}