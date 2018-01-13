package io.iconect.lightbot.domain.hap.service

import io.iconect.lightbot.domain.hap.service.characteristic.Name
import io.iconect.lightbot.domain.hap.service.characteristic.On
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LightbulbTest {

    @Test
    fun `verify predefined values`() {
        val lightbulb = Lightbulb(1, 2, 3, 4)

        assertThat(lightbulb.instanceId).isEqualTo(1)
        assertThat(lightbulb.accessoryInstanceId).isEqualTo(2)
        assertThat(lightbulb.uuid).isEqualTo("00000043-0000-1000-8000-0026BB765291")
        assertThat(lightbulb.type).isEqualTo("public.hap.service.lightbulb")
        assertThat(lightbulb.hidden).isFalse()
        assertThat(lightbulb.primaryService).isTrue()
        assertThat(lightbulb.linkedServices).isEmpty()
        assertThat(lightbulb.characteristics.filter { c -> c is On }.size).isEqualTo(1)
        assertThat(lightbulb.characteristics.filter { c -> c is Name }.size).isEqualTo(1)

        val on = lightbulb.characteristics.first { c -> c is On } as On
        assertThat(on.instanceId).isEqualTo(3)

        val name = lightbulb.characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(4)
    }

}