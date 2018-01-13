package io.iconect.lightbot.domain.hap

import io.iconect.lightbot.domain.hap.service.LightBulb
import io.iconect.lightbot.domain.hap.service.Thermostat
import io.iconect.lightbot.domain.hap.service.Window
import io.iconect.lightbot.domain.hap.service.characteristic.Name
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AccessoryFactory @Autowired constructor(private val characteristicAdjustedEventRepository: CharacteristicAdjustedEventRepository) {

    fun createLightBulbAccessory(accessoryInstanceId: Int,
                                 instanceId: Int,
                                 onInstanceId: Int,
                                 nameInstanceId: Int,
                                 name: String): Accessory {
        val eventPublisher: (Int, Int, String) -> Unit = { aid, iid, value -> characteristicAdjustedEventRepository.pushEvent(CharacteristicAdjustedEvent(aid, iid, value)) }
        val lightBulb = LightBulb(instanceId, accessoryInstanceId, onInstanceId, nameInstanceId, eventPublisher)
        (lightBulb.characteristics.first { c -> c is Name } as Name).updateName(name)
        return Accessory(accessoryInstanceId, listOf(lightBulb))
    }

    fun createThermostatAccessory(accessoryInstanceId: Int,
                                  instanceId: Int,
                                  targetTemperatureInstanceId: Int,
                                  currentTemperatureInstanceId: Int,
                                  nameInstanceId: Int,
                                  name: String): Accessory {
        val eventPublisher: (Int, Int, String) -> Unit = { aid, iid, value -> characteristicAdjustedEventRepository.pushEvent(CharacteristicAdjustedEvent(aid, iid, value)) }
        val thermostat = Thermostat(instanceId, accessoryInstanceId, targetTemperatureInstanceId, currentTemperatureInstanceId, nameInstanceId, eventPublisher)
        (thermostat.characteristics.first { c -> c is Name } as Name).updateName(name)
        return Accessory(accessoryInstanceId, listOf(thermostat))
    }

    fun createWindowAccessory(accessoryInstanceId: Int,
                              instanceId: Int,
                              targetPositionInstanceId: Int,
                              currentPositionInstanceId: Int,
                              nameInstanceId: Int,
                              name: String): Accessory {
        val eventPublisher: (Int, Int, String) -> Unit = { aid, iid, value -> characteristicAdjustedEventRepository.pushEvent(CharacteristicAdjustedEvent(aid, iid, value)) }
        val window = Window(instanceId, accessoryInstanceId, targetPositionInstanceId, currentPositionInstanceId, nameInstanceId, eventPublisher)
        (window.characteristics.first { c -> c is Name } as Name).updateName(name)
        return Accessory(accessoryInstanceId, listOf(window))
    }

}