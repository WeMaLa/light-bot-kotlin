package io.iconect.lightbot.infrastructure.model

import com.fasterxml.jackson.annotation.JsonInclude
import io.iconect.lightbot.domain.Accessory

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccessoryDto constructor(val aid: Int, val services: List<ServiceDto>) {

    companion object {
        fun from(accessory: Accessory) : AccessoryDto {
            return AccessoryDto(accessory.instanceId, accessory.services.map { s -> ServiceDto.from(s) })
        }
    }

}