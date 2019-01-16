package chat.to.lightbot.domain.hap.service.characteristic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OnTest {

    @Test
    fun `verify predefined values`() {
        val on = On(1, 2, { _, _, _ -> })

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
        val on = On(3, 1, { _, _, _ -> })
        on.adjustValue("on")

        assertThat(on.instanceId).isEqualTo(3)
        assertThat(on.value).isEqualTo("on")

        on.adjustValue("off")
        assertThat(on.value).isEqualTo("off")
    }

    @Test
    fun `adjust value and check event is thrown`() {
        var eventAccessoryId: Int? = null
        var eventCharacteristicsId: Int? = null
        var eventValue: String? = null

        val on = On(3, 1, { accessoryInstanceId, characteristicInstanceId, value ->
            run {
                eventAccessoryId = accessoryInstanceId
                eventCharacteristicsId = characteristicInstanceId
                eventValue = value
            }
        })
        on.adjustValue("on")
        assertThat(eventAccessoryId).isEqualTo(1)
        assertThat(eventCharacteristicsId).isEqualTo(3)
        assertThat(eventValue).isEqualTo("on")

        on.adjustValue("off")
        assertThat(eventAccessoryId).isEqualTo(1)
        assertThat(eventCharacteristicsId).isEqualTo(3)
        assertThat(eventValue).isEqualTo("off")
    }

    @Test
    fun `verify event is not thrown when status not has been changed`() {
        var eventAccessoryId: Int? = null
        var eventCharacteristicsId: Int? = null
        var eventValue: String? = null

        val on = On(3, 1, { accessoryInstanceId, characteristicInstanceId, value ->
            run {
                eventAccessoryId = accessoryInstanceId
                eventCharacteristicsId = characteristicInstanceId
                eventValue = value
            }
        })

        on.adjustValue("off")
        assertThat(eventAccessoryId).isNull()
        assertThat(eventCharacteristicsId).isNull()
        assertThat(eventValue).isNull()
    }

    @Test
    fun `adjust value is no on or off value`() {
        val on = On(3, 1, { _, _, _ -> })
        assertThrows<IllegalArgumentException> { on.adjustValue("no-double") }
    }

}