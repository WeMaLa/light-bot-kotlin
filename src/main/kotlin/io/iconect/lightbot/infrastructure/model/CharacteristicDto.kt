package io.iconect.lightbot.infrastructure.model

import io.iconect.lightbot.domain.service.characteristic.Characteristic

data class CharacteristicDto(val iid: Int, val type: String) {

    companion object {
        fun from(characteristic: Characteristic): CharacteristicDto {
            return CharacteristicDto(characteristic.instanceId, UuidToTypeMapper.map(characteristic.uuid))
        }
    }

}