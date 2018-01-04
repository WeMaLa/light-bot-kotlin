package io.iconect.lightbot.infrastructure.model

import com.fasterxml.jackson.annotation.JsonInclude
import io.iconect.lightbot.domain.hap.Accessory
import io.swagger.annotations.ApiModelProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccessoryDto constructor(
        @ApiModelProperty(value = "Integer assigned by the HAP Accessory Server to uniquely identify the HAP Accessory object, see Instance IDs (page 30).", required = true, example = "1") val aid: Int,
        @ApiModelProperty(value = "Array of Service objects. Must not be empty. The maximum number of services must not exceed 100.", required = true)val services: List<ServiceDto>) {

    companion object {
        fun from(accessory: Accessory) : AccessoryDto {
            return AccessoryDto(accessory.instanceId, accessory.services.map { s -> ServiceDto.from(s) })
        }
    }

}