package io.iconect.lightbot.infrastructure.model

import org.springframework.hateoas.ResourceSupport
import java.io.Serializable

data class RoomDto constructor(var identifier: String? = null, var designation: String? = null) : Serializable, ResourceSupport()