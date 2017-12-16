package io.iconect.lightbot.domain.hap.service

import io.iconect.lightbot.domain.hap.service.characteristic.Characteristic

// page 64
interface Service {

    val type: String
    val instanceId: Int
    val characteristics: List<Characteristic>
    val hidden: Boolean
    val primaryService: Boolean
    val linkedServices: List<Int>

}