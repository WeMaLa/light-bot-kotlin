package io.iconect.lightbot.domain

import io.iconect.lightbot.domain.service.Service

data class Accessory constructor(val instanceId: Int, val services: List<Service>)