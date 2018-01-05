package io.iconect.lightbot.domain.hap

import io.iconect.lightbot.domain.hap.service.Thermostat
import io.iconect.lightbot.domain.hap.service.characteristic.Name
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AccessoryTest {

    @Test
    fun `find characteristic by iid`() {
        val kitchenThermostat = Thermostat(2, 21, 22, 23)
        (kitchenThermostat.characteristics.first { c -> c is Name } as Name).updateName("Kitchen thermostat heater")
        val accessory = Accessory(1, listOf(kitchenThermostat))

        assertThat(accessory.findCharacteristic(21)).isNotNull()
        assertThat(accessory.findCharacteristic(21)!!.instanceId).isEqualTo(21)
        assertThat(accessory.findCharacteristic(22)).isNotNull()
        assertThat(accessory.findCharacteristic(22)!!.instanceId).isEqualTo(22)
        assertThat(accessory.findCharacteristic(23)).isNotNull()
        assertThat(accessory.findCharacteristic(23)!!.instanceId).isEqualTo(23)
        assertThat(accessory.findCharacteristic(24)).isNull()
    }
}