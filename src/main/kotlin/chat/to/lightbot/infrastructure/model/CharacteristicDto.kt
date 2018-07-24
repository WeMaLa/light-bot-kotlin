package chat.to.lightbot.infrastructure.model

import com.fasterxml.jackson.annotation.JsonInclude
import chat.to.lightbot.domain.hap.service.characteristic.Characteristic
import io.swagger.annotations.ApiModelProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CharacteristicDto(
        @ApiModelProperty(value = "Integer assigned by the HAP Accessory Server to uniquely identify the HAP Service object, see Instance IDs (page 30).", required = true, example = "5") val iid: Int,
        @ApiModelProperty(value = "String that defines the type of the characteristic. See Service and Characteristic Types (page 72).", required = true, example = "22") val type: String,
        @ApiModelProperty(value = "The value of the characteristic, which must conform to the \"format\" property. The literal value null may also be used if the characteristic has no value. This property must be present if and only if the characteristic contains the Paired Read permission, see Table 5-4 (page 67).", required = true, example = "Bridge1,1") val value: String?,
        @ApiModelProperty(value = "Format of the value, e.g. \"float\". See Table 5-5 (page 67).", required = true, example = "string") val format: String,
        @ApiModelProperty(value = "Array of permission strings describing the capabilities of the characteristic. See Table 5-4 (page 67).", required = true, example = "[ \"pr\" ]") val perms: List<String>) {

    companion object {
        fun from(characteristic: Characteristic): CharacteristicDto {
            return CharacteristicDto(characteristic.instanceId, reduceUuidToTheFirstPartAndRemoveLeadingZeros(characteristic.uuid), characteristic.value, characteristic.format.json, characteristic.permissions.map { it.json })
        }
    }

}