package io.iconect.lightbot.infrastructure.model;

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "ErrorMessageDto", description = "Contains defined error message")
data class ErrorMessageDto constructor(
        @ApiModelProperty(value = "specific error message", required = true, example = "Room not found.") val message: String)
