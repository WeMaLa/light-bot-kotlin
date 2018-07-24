package chat.to.lightbot.domain.hap

import chat.to.lightbot.domain.hap.service.Service
import chat.to.lightbot.domain.hap.service.characteristic.Characteristic

data class Accessory constructor(val instanceId: Int, val services: List<Service>) {

    fun findCharacteristic(iid: Int): Characteristic? {
        return services
                .flatMap { it.characteristics }
                .find { it.instanceId == iid }
    }
}