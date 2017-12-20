package io.iconect.lightbot.infrastructure.model

import io.iconect.lightbot.domain.service.Service

data class ServiceDto(val iid: Int, val type: String) {

    companion object {
        fun from(service: Service): ServiceDto {
            return ServiceDto(service.instanceId, UuidToTypeMapper.map(service.uuid))
        }
    }

}