package io.iconect.lightbot.infrastructure.model

import io.swagger.annotations.ApiModelProperty

data class DefaultSpringErrorDto constructor(
        @ApiModelProperty(value = "Timestamp in milles when the error occurred", required = true, example = "14443434344") val timestamp: String,
        @ApiModelProperty(value = "Http status", required = true, example = "405") val status: String,
        @ApiModelProperty(value = "Short error message", required = true, example = "Method Not Allowed") val error: String,
        @ApiModelProperty(value = "returning exception", required = true, example = "org.springframework.web.HttpRequestMethodNotSupportedException") val exception: String,
        @ApiModelProperty(value = "specific error message", required = true, example = "Request method 'DELETE' not supported") val message: String,
        @ApiModelProperty(value = "requested url", required = true, example = "/api/room/kitchen-1") val path: String

)