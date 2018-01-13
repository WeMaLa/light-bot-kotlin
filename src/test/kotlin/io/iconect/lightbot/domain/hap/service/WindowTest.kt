package io.iconect.lightbot.domain.hap.service

import io.iconect.lightbot.domain.hap.service.characteristic.CurrentPosition
import io.iconect.lightbot.domain.hap.service.characteristic.Name
import io.iconect.lightbot.domain.hap.service.characteristic.TargetPosition
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class WindowTest {

    @Test
    fun `verify predefined values`() {
        val window = Window(1, 2, 3, 4, 5)

        assertThat(window.instanceId).isEqualTo(1)
        assertThat(window.accessoryInstanceId).isEqualTo(2)
        assertThat(window.uuid).isEqualTo("0000008B-0000-1000-8000-0026BB765291")
        assertThat(window.type).isEqualTo("public.hap.service.window")
        assertThat(window.hidden).isFalse()
        assertThat(window.primaryService).isTrue()
        assertThat(window.linkedServices).isEmpty()
        assertThat(window.characteristics.filter { c -> c is TargetPosition }.size).isEqualTo(1)
        assertThat(window.characteristics.filter { c -> c is CurrentPosition }.size).isEqualTo(1)
        assertThat(window.characteristics.filter { c -> c is Name }.size).isEqualTo(1)

        val targetTemperature = window.characteristics.first { c -> c is TargetPosition } as TargetPosition
        assertThat(targetTemperature.instanceId).isEqualTo(3)

        val currentTemperature = window.characteristics.first { c -> c is CurrentPosition } as CurrentPosition
        assertThat(currentTemperature.instanceId).isEqualTo(4)

        val name = window.characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(5)
    }

}