package io.iconect.lightbot.domain.hap.service

import io.iconect.lightbot.domain.hap.service.characteristic.Name
import io.iconect.lightbot.domain.hap.service.characteristic.On

// service page 218
data class Lightbulb(override val instanceId: Int,
                     private val onInstanceId: Int,
                     private val nameInstanceId: Int) : Service {

    override val uuid = "00000043-0000-1000-8000-0026BB765291"
    override val type = "public.hap.service.lightbulb"
    override val characteristics = listOf(
            On(onInstanceId),
            Name(nameInstanceId))
    override val hidden = false
    override val primaryService = true
    override val linkedServices = emptyList<Int>()

}