package io.iconect.lightbot.domain.hap

import io.iconect.lightbot.domain.hap.service.LightBulb
import io.iconect.lightbot.domain.hap.service.Thermostat
import io.iconect.lightbot.domain.hap.service.Window
import io.iconect.lightbot.domain.hap.service.characteristic.Name
import org.springframework.stereotype.Component

@Component
class AccessoryFactory {

    fun createLightBulbAccessory(accessoryInstanceId: Int,
                                 instanceId: Int,
                                 onInstanceId: Int,
                                 nameInstanceId: Int,
                                 name: String): Accessory {
        val lightBulb = LightBulb(instanceId, accessoryInstanceId, onInstanceId, nameInstanceId, { _, _, _ -> })
        (lightBulb.characteristics.first { c -> c is Name } as Name).updateName(name)
        return Accessory(accessoryInstanceId, listOf(lightBulb))
    }

    fun createThermostatAccessory(accessoryInstanceId: Int,
                                  instanceId: Int,
                                  targetTemperatureInstanceId: Int,
                                  currentTemperatureInstanceId: Int,
                                  nameInstanceId: Int,
                                  name: String): Accessory {
        val thermostat = Thermostat(instanceId, accessoryInstanceId, targetTemperatureInstanceId, currentTemperatureInstanceId, nameInstanceId, { _, _, _ -> })
        (thermostat.characteristics.first { c -> c is Name } as Name).updateName(name)
        return Accessory(accessoryInstanceId, listOf(thermostat))
    }

    fun createWindowAccessory(accessoryInstanceId: Int,
                              instanceId: Int,
                              targetPositionInstanceId: Int,
                              currentPositionInstanceId: Int,
                              nameInstanceId: Int,
                              name: String): Accessory {
        val window = Window(instanceId, accessoryInstanceId, targetPositionInstanceId, currentPositionInstanceId, nameInstanceId, { _, _, _ -> })
        (window.characteristics.first { c -> c is Name } as Name).updateName(name)
        return Accessory(accessoryInstanceId, listOf(window))
    }

}