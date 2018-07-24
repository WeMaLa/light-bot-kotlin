package chat.to.lightbot.domain.hap.service.characteristic

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CurrentPositionTest {

    @Test
    fun `verify predefined values`() {
        val currentPosition = CurrentPosition(1, 2, { _, _, _ -> })

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
        val currentPosition = CurrentPosition(3, 1, { _, _, _ -> })
        currentPosition.adjustValue(23)

        assertThat(currentPosition.instanceId).isEqualTo(3)
        assertThat(currentPosition.value).isEqualTo("23")

        currentPosition.adjustValue(0)
        assertThat(currentPosition.value).isEqualTo("0")

        currentPosition.adjustValue(100)
        assertThat(currentPosition.value).isEqualTo("100")
    }

    @Test
    fun `adjust value and check event is thrown`() {
        var eventAccessoryId: Int? = null
        var eventCharacteristicsId: Int? = null
        var eventValue: String? = null

        val currentPosition = CurrentPosition(3, 1, { accessoryInstanceId, characteristicInstanceId, value ->
            run {
                eventAccessoryId = accessoryInstanceId
                eventCharacteristicsId = characteristicInstanceId
                eventValue = value
            }
        })
        currentPosition.adjustValue(50)
        Assertions.assertThat(eventAccessoryId).isEqualTo(1)
        Assertions.assertThat(eventCharacteristicsId).isEqualTo(3)
        Assertions.assertThat(eventValue).isEqualTo("50")
    }

    @Test
    fun `verify event is not thrown when status not has been changed`() {
        var eventAccessoryId: Int? = null
        var eventCharacteristicsId: Int? = null
        var eventValue: String? = null

        val currentPosition = CurrentPosition(3, 1, { accessoryInstanceId, characteristicInstanceId, value ->
            run {
                eventAccessoryId = accessoryInstanceId
                eventCharacteristicsId = characteristicInstanceId
                eventValue = value
            }
        })
        currentPosition.adjustValue(0)
        Assertions.assertThat(eventAccessoryId).isNull()
        Assertions.assertThat(eventCharacteristicsId).isNull()
        Assertions.assertThat(eventValue).isNull()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value to low`() {
        val targetTemperature = CurrentPosition(3, 1, { _, _, _ -> })
        targetTemperature.adjustValue(-1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust value to high`() {
        val targetTemperature = CurrentPosition(3, 1, { _, _, _ -> })
        targetTemperature.adjustValue(101)
    }
}