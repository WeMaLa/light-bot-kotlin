package io.iconect.lightbot.infrastructure.model

import com.fasterxml.jackson.annotation.JsonInclude
import io.iconect.lightbot.domain.hap.service.Service
import io.swagger.annotations.ApiModelProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ServiceDto(
        @ApiModelProperty(value = "Integer assigned by the HAP Accessory Server to uniquely identify the HAP Service object, see Instance IDs (page 30).", required = true, example = "1") val iid: Int,
        @ApiModelProperty(value = "String that defines the type of the service. See Service and Characteristic Types (page 72).", required = true, example = "3E") val type: String,
        @ApiModelProperty(value = "Array of Characteristic objects. Must not be empty. The maximum number of characteristics must not exceed 100, and each characteristic in the array must have a unique type.", required = true) val characteristics: List<CharacteristicDto>) {

    companion object {
        fun from(service: Service): ServiceDto {
            return ServiceDto(service.instanceId, UuidToTypeMapper.map(service.uuid), service.characteristics.map { CharacteristicDto.from(it) })
        }
    }

}