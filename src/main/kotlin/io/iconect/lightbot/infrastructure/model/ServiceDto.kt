package io.iconect.lightbot.infrastructure.model

import com.fasterxml.jackson.annotation.JsonInclude
import io.iconect.lightbot.domain.service.Service

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ServiceDto(val iid: Int, val type: String, val characteristics: List<CharacteristicDto>) {

    companion object {
        fun from(service: Service): ServiceDto {
            return ServiceDto(service.instanceId, UuidToTypeMapper.map(service.uuid), service.characteristics.map { CharacteristicDto.from(it) })
        }
    }

}