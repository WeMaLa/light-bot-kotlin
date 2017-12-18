package io.iconect.lightbot.infrastructure.model

import io.iconect.lightbot.domain.service.Service

data class ServiceDto(val iid: Int, val type: String) {

    companion object {
        fun from(service: Service): ServiceDto {
            val type = service.uuid.split("-")[0].trimStart('0')
            return ServiceDto(service.instanceId, type)
        }
    }

}