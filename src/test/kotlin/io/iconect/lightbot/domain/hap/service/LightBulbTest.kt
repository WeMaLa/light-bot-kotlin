package io.iconect.lightbot.domain.hap.service

import io.iconect.lightbot.domain.hap.service.characteristic.Name
import io.iconect.lightbot.domain.hap.service.characteristic.On
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LightBulbTest {

    @Test
    fun `verify predefined values`() {
        val lightBulb = LightBulb(1, 2, 3, 4, { _, _, _ -> })

        assertThat(lightBulb.instanceId).isEqualTo(1)
        assertThat(lightBulb.accessoryInstanceId).isEqualTo(2)
        assertThat(lightBulb.uuid).isEqualTo("00000043-0000-1000-8000-0026BB765291")
        assertThat(lightBulb.type).isEqualTo("public.hap.service.lightbulb")
        assertThat(lightBulb.hidden).isFalse()
        assertThat(lightBulb.primaryService).isTrue()
        assertThat(lightBulb.linkedServices).isEmpty()
        assertThat(lightBulb.characteristics.filter { c -> c is On }.size).isEqualTo(1)
        assertThat(lightBulb.characteristics.filter { c -> c is Name }.size).isEqualTo(1)

        val on = lightBulb.characteristics.first { c -> c is On } as On
        assertThat(on.instanceId).isEqualTo(3)

        val name = lightBulb.characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(4)
    }

}