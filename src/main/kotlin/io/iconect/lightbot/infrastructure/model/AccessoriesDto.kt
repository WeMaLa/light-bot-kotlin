package io.iconect.lightbot.infrastructure.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccessoriesDto constructor(val accessories: List<AccessoryDto>)