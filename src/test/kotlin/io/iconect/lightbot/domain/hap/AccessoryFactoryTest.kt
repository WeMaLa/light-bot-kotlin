package io.iconect.lightbot.domain.hap

import io.iconect.lightbot.domain.hap.service.characteristic.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AccessoryFactoryTest {

    private val accessoryFactory: AccessoryFactory = AccessoryFactory()

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
    }

    @Test
    fun `create thermostat accessory`() {
        val accessory = accessoryFactory.createThermostatAccessory(1, 2, 3, 4, 5, "unit-test-light-bulb")

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
    }

    @Test
    fun `create window accessory`() {
        val accessory = accessoryFactory.createWindowAccessory(1, 2, 3, 4, 5, "unit-test-light-bulb")

        assertThat(accessory.instanceId).isEqualTo(1)
        assertThat(accessory.services.size).isEqualTo(1)
        assertThat(accessory.services[0].instanceId).isEqualTo(2)
        assertThat(accessory.services[0].accessoryInstanceId).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.size).isEqualTo(3)
        assertThat(accessory.services[0].characteristics.filter { c -> c is TargetPosition }.size).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.filter { c -> c is CurrentPosition }.size).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.filter { c -> c is Name }.size).isEqualTo(1)

        val targetTemperature = accessory.services[0].characteristics.first { c -> c is TargetPosition } as TargetPosition
        assertThat(targetTemperature.instanceId).isEqualTo(3)

        val currentTemperature = accessory.services[0].characteristics.first { c -> c is CurrentPosition } as CurrentPosition
        assertThat(currentTemperature.instanceId).isEqualTo(4)

        val name = accessory.services[0].characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(5)
    }
}