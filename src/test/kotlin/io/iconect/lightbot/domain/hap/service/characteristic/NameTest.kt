package io.iconect.lightbot.domain.hap.service.characteristic

import org.assertj.core.api.Assertions
import org.junit.Test

class NameTest {

    @Test
    fun `verify predefined values`() {
        val name = Name(1, 2)

        Assertions.assertThat(name.instanceId).isEqualTo(1)
        Assertions.assertThat(name.accessoryInstanceId).isEqualTo(2)
        Assertions.assertThat(name.uuid).isEqualTo("00000023-0000-1000-8000-0026BB765291")
        Assertions.assertThat(name.type).isEqualTo("public.hap.characteristic.temperature.name")
        Assertions.assertThat(name.description).isNull()
        Assertions.assertThat(name.value).isNull()
        Assertions.assertThat(name.permissions).containsOnly(Permission.PAIRED_READ)
        Assertions.assertThat(name.format).isEqualTo(Format.STRING)
        Assertions.assertThat(name.unit).isNull()
        Assertions.assertThat(name.minimumValue).isNull()
        Assertions.assertThat(name.maximumValue).isNull()
        Assertions.assertThat(name.stepValue).isNull()
        Assertions.assertThat(name.maximumLength).isEqualTo(64)
        Assertions.assertThat(name.maxDataLength).isNull()
    }

    @Test
    fun `adjust value`() {
        val name = Name(3, 1)
        name.updateName("")

        Assertions.assertThat(name.instanceId).isEqualTo(3)
        Assertions.assertThat(name.value).isEqualTo("")

        name.updateName("kitchen")
        Assertions.assertThat(name.value).isEqualTo("kitchen")

        name.updateName("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz111111111111")
        Assertions.assertThat(name.value).isEqualTo("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz111111111111")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `update name with too many characters`() {
        val name = Name(4, 1)
        name.updateName("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz1111111111112")
    }

}