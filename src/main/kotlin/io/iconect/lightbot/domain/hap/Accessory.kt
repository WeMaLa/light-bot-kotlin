package io.iconect.lightbot.domain.hap

import io.iconect.lightbot.domain.hap.service.Service
import io.iconect.lightbot.domain.hap.service.characteristic.Characteristic

data class Accedssory constructor(val instanceId: Int, val services: List<Service>) {

    fun findCharacteristic(iid: Int): Characteristic? {
        return services
                .flatMap { it.characteristics }
                .find { it.instanceId == iid }
    }
}