package io.iconect.lightbot.domain.hap.service.characteristic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CurrentPositionTest {

    @Test
    fun `verify predefined values`() {
        val currentPosition = CurrentPosition(1, 2)

        assertThat(currentPosition.instanceId).isEqualTo(1)
        assertThat(currentPosition.accessoryInstanceId).isEqualTo(2)
        assertThat(currentPosition.uuid).isEqualTo("0000006D-0000-1000-8000-0026BB765291")
        assertThat(currentPosition.type).isEqualTo("public.hap.characteristic.position.current")
        assertThat(currentPosition.description).isNull()
        assertThat(currentPosition.value).isEqualTo("0")
        assertThat(currentPosition.permissions).containsOnly(Permission.PAIRED_READ, Permission.NOTIFY)
        assertThat(currentPosition.format).isEqualTo(Format.UINT8)
        assertThat(currentPosition.unit).isEqualTo(Unit.PERCENTAGE)
        assertThat(currentPosition.minimumValue).isEqualTo(0.0)
        assertThat(currentPosition.maximumValue).isEqualTo(100.0)
        assertThat(currentPosition.stepValue).isEqualTo(1.0)
        assertThat(currentPosition.maximumLength).isNull()
        assertThat(currentPosition.maxDataLength).isNull()
    }

    @Test
    fun `adjust value`() {
        val currentPosition = CurrentPosition(3, 1)
        currentPosition.adjustValue(23)

        assertThat(currentPosition.instanceId).isEqualTo(3)
        assertThat(currentPosition.value).isEqualTo("23")

        currentPosition.adjustValue(0)
        assertThat(currentPosition.value).isEqualTo("0")

        currentPosition.adjustValue(100)
        assertThat(currentPosition.value).isEqualTo("100")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value to low`() {
        val targetTemperature = CurrentPosition(3, 1)
        targetTemperature.adjustValue(-1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value to high`() {
        val targetTemperature = CurrentPosition(3, 1)
        targetTemperature.adjustValue(101)
    }
}