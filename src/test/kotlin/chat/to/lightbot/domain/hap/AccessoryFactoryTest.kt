package chat.to.lightbot.domain.hap

import chat.to.lightbot.domain.hap.service.characteristic.*
import chat.to.lightbot.infrastructure.CachedCharacteristicAdjustedEventRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AccessoryFactoryTest {

    private val eventRepository = CachedCharacteristicAdjustedEventRepository()
    private val accessoryFactory: AccessoryFactory = AccessoryFactory(eventRepository)

    @Test
    fun `create light bulb accessory`() {
        val accessory = accessoryFactory.createLightBulbAccessory(1, 2, 3, 4, "unit-test-light-bulb")

        assertThat(accessory.instanceId).isEqualTo(1)
        assertThat(accessory.services.size).isEqualTo(1)
        assertThat(accessory.services[0].instanceId).isEqualTo(2)
        assertThat(accessory.services[0].accessoryInstanceId).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.size).isEqualTo(2)
        assertThat(accessory.services[0].characteristics.filter { c -> c is On }.size).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.filter { c -> c is Name }.size).isEqualTo(1)

        val on = accessory.services[0].characteristics.first { c -> c is On } as On
        assertThat(on.instanceId).isEqualTo(3)

        val name = accessory.services[0].characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(4)

        var popNextEvent = eventRepository.popNextEvent()
        assertThat(popNextEvent?.accessoryId).isEqualTo(1)
        assertThat(popNextEvent?.characteristicId).isEqualTo(4)
        assertThat(popNextEvent?.value).isEqualTo("unit-test-light-bulb")

        on.adjustValue("on")
        popNextEvent = eventRepository.popNextEvent()
        assertThat(popNextEvent?.accessoryId).isEqualTo(1)
        assertThat(popNextEvent?.characteristicId).isEqualTo(3)
        assertThat(popNextEvent?.value).isEqualTo("on")

        assertThat(eventRepository.popNextEvent()).isNull()
    }

    @Test
    fun `create thermostat accessory`() {
        val accessory = accessoryFactory.createThermostatAccessory(1, 2, 3, 4, 5, "unit-test-thermostat")

        assertThat(accessory.instanceId).isEqualTo(1)
        assertThat(accessory.services.size).isEqualTo(1)
        assertThat(accessory.services[0].instanceId).isEqualTo(2)
        assertThat(accessory.services[0].accessoryInstanceId).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.size).isEqualTo(3)
        assertThat(accessory.services[0].characteristics.filter { c -> c is TargetTemperature }.size).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.filter { c -> c is CurrentTemperature }.size).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.filter { c -> c is Name }.size).isEqualTo(1)

        val targetTemperature = accessory.services[0].characteristics.first { c -> c is TargetTemperature } as TargetTemperature
        assertThat(targetTemperature.instanceId).isEqualTo(3)

        val currentTemperature = accessory.services[0].characteristics.first { c -> c is CurrentTemperature } as CurrentTemperature
        assertThat(currentTemperature.instanceId).isEqualTo(4)

        val name = accessory.services[0].characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(5)

        var popNextEvent = eventRepository.popNextEvent()
        assertThat(popNextEvent?.accessoryId).isEqualTo(1)
        assertThat(popNextEvent?.characteristicId).isEqualTo(5)
        assertThat(popNextEvent?.value).isEqualTo("unit-test-thermostat")

        targetTemperature.adjustValue("19.8")
        popNextEvent = eventRepository.popNextEvent()
        assertThat(popNextEvent?.accessoryId).isEqualTo(1)
        assertThat(popNextEvent?.characteristicId).isEqualTo(3)
        assertThat(popNextEvent?.value).isEqualTo("19.8")

        assertThat(eventRepository.popNextEvent()).isNull()
    }

    @Test
    fun `create window accessory`() {
        val accessory = accessoryFactory.createWindowAccessory(1, 2, 3, 4, 5, "unit-test-window")

        assertThat(accessory.instanceId).isEqualTo(1)
        assertThat(accessory.services.size).isEqualTo(1)
        assertThat(accessory.services[0].instanceId).isEqualTo(2)
        assertThat(accessory.services[0].accessoryInstanceId).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.size).isEqualTo(3)
        assertThat(accessory.services[0].characteristics.filter { c -> c is TargetPosition }.size).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.filter { c -> c is CurrentPosition }.size).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.filter { c -> c is Name }.size).isEqualTo(1)

        val targetPosition = accessory.services[0].characteristics.first { c -> c is TargetPosition } as TargetPosition
        assertThat(targetPosition.instanceId).isEqualTo(3)

        val currentPosition = accessory.services[0].characteristics.first { c -> c is CurrentPosition } as CurrentPosition
        assertThat(currentPosition.instanceId).isEqualTo(4)

        val name = accessory.services[0].characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(5)

        var popNextEvent = eventRepository.popNextEvent()
        assertThat(popNextEvent?.accessoryId).isEqualTo(1)
        assertThat(popNextEvent?.characteristicId).isEqualTo(5)
        assertThat(popNextEvent?.value).isEqualTo("unit-test-window")

        targetPosition.adjustValue("19")
        popNextEvent = eventRepository.popNextEvent()
        assertThat(popNextEvent?.accessoryId).isEqualTo(1)
        assertThat(popNextEvent?.characteristicId).isEqualTo(3)
        assertThat(popNextEvent?.value).isEqualTo("19")

        assertThat(eventRepository.popNextEvent()).isNull()
    }
}