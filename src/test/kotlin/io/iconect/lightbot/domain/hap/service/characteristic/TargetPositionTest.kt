package io.iconect.lightbot.domain.hap.service.characteristic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TargetPositionTest {

    @Test
    fun `verify predefined values`() {
        val targetPosition = TargetPosition(1)

        assertThat(targetPosition.instanceId).isEqualTo(1)
        assertThat(targetPosition.uuid).isEqualTo("0000007C-0000-1000-8000-0026BB765291")
        assertThat(targetPosition.type).isEqualTo("public.hap.characteristic.position.target")
        assertThat(targetPosition.description).isNull()
        assertThat(targetPosition.value).isEqualTo("0")
        assertThat(targetPosition.permissions).containsOnly(Permission.PAIRED_READ, Permission.PAIRED_WRITE, Permission.NOTIFY)
        assertThat(targetPosition.format).isEqualTo(Format.UINT8)
        assertThat(targetPosition.unit).isEqualTo(Unit.PERCENTAGE)
        assertThat(targetPosition.minimumValue).isEqualTo(0.0)
        assertThat(targetPosition.maximumValue).isEqualTo(100.0)
        assertThat(targetPosition.stepValue).isEqualTo(1.0)
        assertThat(targetPosition.maximumLength).isNull()
        assertThat(targetPosition.maxDataLength).isNull()
    }

    @Test
    fun `adjust value`() {
        val targetPosition = TargetPosition(3)
        targetPosition.adjustValue("23")

        assertThat(targetPosition.instanceId).isEqualTo(3)
        assertThat(targetPosition.value).isEqualTo("23")

        targetPosition.adjustValue("0")
        assertThat(targetPosition.value).isEqualTo("0")

        targetPosition.adjustValue("100")
        assertThat(targetPosition.value).isEqualTo("100")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value to low`() {
        val targetPosition = TargetPosition(3)
        targetPosition.adjustValue("-1")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value to high`() {
        val targetPosition = TargetPosition(3)
        targetPosition.adjustValue("101")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value is no int value`() {
        val targetPosition = TargetPosition(3)
        targetPosition.adjustValue("no-double")
    }
}