package io.iconect.lightbot.infrastructure.model

import java.io.Serializable

data class RoomDto constructor(var identifier: String? = null, var designation: String? = null) : Serializable