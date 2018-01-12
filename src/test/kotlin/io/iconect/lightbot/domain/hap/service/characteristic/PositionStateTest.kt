package io.iconect.lightbot.domain.hap.service.characteristic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PositionStateTest {

    @Test
    fun `verify predefined values`() {
        val positionState = PositionState(1)

        assertThat(positionState.instanceId).isEqualTo(1)
        assertThat(positionState.uuid).isEqualTo("00000072-0000-1000-8000-0026BB765291")
        assertThat(positionState.type).isEqualTo("public.hap.characteristic.position.state")
        assertThat(positionState.description).isNull()
        assertThat(positionState.value).isEqualTo("0")
        assertThat(positionState.permissions).containsOnly(Permission.PAIRED_READ, Permission.NOTIFY)
        assertThat(positionState.format).isEqualTo(Format.UINT8)
        assertThat(positionState.unit).isEqualTo(Unit.OTHER)
        assertThat(positionState.minimumValue).isEqualTo(0.0)
        assertThat(positionState.maximumValue).isEqualTo(2.0)
        assertThat(positionState.stepValue).isEqualTo(1.0)
        assertThat(positionState.maximumLength).isNull()
        assertThat(positionState.maxDataLength).isNull()
    }
}