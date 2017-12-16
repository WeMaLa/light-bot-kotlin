package io.iconect.lightbot.domain.hap

import io.iconect.lightbot.domain.hap.service.Service

data class Accessory constructor(val instanceId: Int, val services: List<Service>)