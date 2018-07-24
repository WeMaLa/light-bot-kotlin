package chat.to.lightbot.application.hap

import chat.to.lightbot.domain.hap.AccessoryRepository
import chat.to.lightbot.domain.hap.CharacteristicAdjustedEvent
import chat.to.lightbot.domain.hap.service.characteristic.CurrentPosition
import chat.to.lightbot.domain.hap.service.characteristic.CurrentTemperature
import chat.to.lightbot.domain.hap.service.characteristic.TargetPosition
import chat.to.lightbot.domain.hap.service.characteristic.TargetTemperature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.math.abs

@Component
class HapEventHandler @Autowired constructor(private val accessoryRepository: AccessoryRepository) {

    fun handleEvent(event: CharacteristicAdjustedEvent) {
        val characteristic = accessoryRepository.findByInstanceId(event.accessoryId)?.findCharacteristic(event.characteristicId)

        if (characteristic is TargetTemperature) {
            val currentTemperature = getCurrentTemperatureForTargetTemperature(characteristic, event.accessoryId)
            val eventValue = event.value.toDouble()

            while (abs(eventValue - currentTemperature.value.toDouble()) > 1) {
                val nextValue = if (eventValue < currentTemperature.value.toDouble()) currentTemperature.value.toDouble() - 1 else currentTemperature.value.toDouble() + 1
                currentTemperature.adjustValue(nextValue.toInt().toDouble())
            }

            currentTemperature.adjustValue(event.value.toDouble())
        }
        if (characteristic is TargetPosition) {
            val currentPosition = getCurrentPositionForTargetPosition(characteristic, event.accessoryId)
            val eventValue = event.value.toInt()

            while (eventValue != currentPosition.value.toInt()) {
                val nextValue = if (eventValue < currentPosition.value.toInt()) currentPosition.value.toInt() - 1 else currentPosition.value.toInt() + 1
                currentPosition.adjustValue(nextValue)
            }
        }
    }

    private fun getCurrentTemperatureForTargetTemperature(targetTemperature: TargetTemperature, accessoryInstanceId: Int): CurrentTemperature {
        return accessoryRepository.findByInstanceId(accessoryInstanceId)!!
                .services.find { it.characteristics.contains(targetTemperature) }!!
                .characteristics.find { it is CurrentTemperature } as CurrentTemperature
    }

    private fun getCurrentPositionForTargetPosition(targetPosition: TargetPosition, accessoryInstanceId: Int): CurrentPosition {
        return accessoryRepository.findByInstanceId(accessoryInstanceId)!!
                .services.find { it.characteristics.contains(targetPosition) }!!
                .characteristics.find { it is CurrentPosition } as CurrentPosition
    }

}