package io.iconect.lightbot.domain.hap.service.characteristic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class OnTest {

    @Test
    fun `verify predefined values`() {
        val on = On(1, 2)

        assertThat(on.instanceId).isEqualTo(1)
        assertThat(on.accessoryInstanceId).isEqualTo(2)
        assertThat(on.uuid).isEqualTo("00000025-0000-1000-8000-0026BB765291")
        assertThat(on.type).isEqualTo("public.hap.characteristic.on")
        assertThat(on.description).isNull()
        assertThat(on.value).isEqualTo("off")
        assertThat(on.permissions).containsOnly(Permission.PAIRED_READ, Permission.PAIRED_WRITE, Permission.NOTIFY)
        assertThat(on.format).isEqualTo(Format.BOOL)
        assertThat(on.unit).isNull()
        assertThat(on.minimumValue).isNull()
        assertThat(on.maximumValue).isNull()
        assertThat(on.stepValue).isNull()
        assertThat(on.maximumLength).isNull()
        assertThat(on.maxDataLength).isNull()
    }

    @Test
    fun `adjust value`() {
        val on = On(3, 1)
        on.adjustValue("on")

        assertThat(on.instanceId).isEqualTo(3)
        assertThat(on.value).isEqualTo("on")

        on.adjustValue("off")
        assertThat(on.value).isEqualTo("off")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value is no on or off value`() {
        val on = On(3, 1)
        on.adjustValue("no-double")
    }
    
}