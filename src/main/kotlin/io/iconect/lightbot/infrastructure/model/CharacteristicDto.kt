package io.iconect.lightbot.infrastructure.model

import com.fasterxml.jackson.annotation.JsonInclude
import io.iconect.lightbot.domain.service.characteristic.Characteristic

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CharacteristicDto(val iid: Int, val type: String, val value: String?, val format: String, val perms: List<String>) {

    companion object {
        fun from(characteristic: Characteristic): CharacteristicDto {
            return CharacteristicDto(characteristic.instanceId, UuidToTypeMapper.map(characteristic.uuid), characteristic.value, characteristic.format.json, characteristic.permissions.map { it.json })
        }
    }

}