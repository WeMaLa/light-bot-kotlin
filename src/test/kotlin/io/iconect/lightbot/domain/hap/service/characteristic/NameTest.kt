package io.iconect.lightbot.domain.hap.service.characteristic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class NameTest {

    @Test
    fun `verify predefined values`() {
        val name = Name(1, 2, { _, _, _ -> })

        assertThat(name.instanceId).isEqualTo(1)
        assertThat(name.accessoryInstanceId).isEqualTo(2)
        assertThat(name.uuid).isEqualTo("00000023-0000-1000-8000-0026BB765291")
        assertThat(name.type).isEqualTo("public.hap.characteristic.temperature.name")
        assertThat(name.description).isNull()
        assertThat(name.value).isNull()
        assertThat(name.permissions).containsOnly(Permission.PAIRED_READ)
        assertThat(name.format).isEqualTo(Format.STRING)
        assertThat(name.unit).isNull()
        assertThat(name.minimumValue).isNull()
        assertThat(name.maximumValue).isNull()
        assertThat(name.stepValue).isNull()
        assertThat(name.maximumLength).isEqualTo(64)
        assertThat(name.maxDataLength).isNull()
    }

    @Test
    fun `adjust value`() {
        val name = Name(3, 1, { _, _, _ -> })
        name.updateName("")

        assertThat(name.instanceId).isEqualTo(3)
        assertThat(name.value).isEqualTo("")

        name.updateName("kitchen")
        assertThat(name.value).isEqualTo("kitchen")

        name.updateName("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz111111111111")
        assertThat(name.value).isEqualTo("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz111111111111")
    }

    @Test
    fun `adjust value and check event is thrown`() {
        var eventAccessoryId: Int? = null
        var eventCharacteristicsId: Int? = null
        var eventValue: String? = null

        val currentTemperature = Name(3, 1, { accessoryInstanceId, characteristicInstanceId, value ->
            run {
                eventAccessoryId = accessoryInstanceId
                eventCharacteristicsId = characteristicInstanceId
                eventValue = value
            }
        })
        currentTemperature.updateName("kitchen")
        assertThat(eventAccessoryId).isEqualTo(1)
        assertThat(eventCharacteristicsId).isEqualTo(3)
        assertThat(eventValue).isEqualTo("kitchen")
    }

    @Test
    fun `verify event is not thrown when status not has been changed`() {
        var eventListenerCallTimes = 0

        val currentTemperature = Name(3, 1, { accessoryInstanceId, characteristicInstanceId, value ->
            run {
                eventListenerCallTimes++
            }
        })
        currentTemperature.updateName("kitchen")

        currentTemperature.updateName("kitchen")

        assertThat(eventListenerCallTimes).isEqualTo(1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `update name with too many characters`() {
        val name = Name(4, 1, { _, _, _ -> })
        name.updateName("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz1111111111112")
    }

}