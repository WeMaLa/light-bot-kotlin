package io.iconect.lightbot.infrastructure.model

import io.iconect.lightbot.domain.Accessory

data class AccessoryDto constructor(val aid: Int, val services: List<ServiceDto>) {

    companion object {
        fun from(accessory: Accessory) : AccessoryDto {
            return AccessoryDto(accessory.instanceId, accessory.services.map { s -> ServiceDto.from(s) })
        }
    }

}