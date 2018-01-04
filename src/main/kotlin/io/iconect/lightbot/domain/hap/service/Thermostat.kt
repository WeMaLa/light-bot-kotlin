package io.iconect.lightbot.domain.hap.service

import io.iconect.lightbot.domain.hap.service.characteristic.CurrentTemperature
import io.iconect.lightbot.domain.hap.service.characteristic.Name
import io.iconect.lightbot.domain.hap.service.characteristic.TargetTemperature

// service page 220
data class Thermostat(override val instanceId: Int,
                      private val targetTemperatureInstanceId: Int,
                      private val currentTemperatureInstanceId: Int,
                      private val nameInstanceId: Int) : Service {

    override val uuid = "0000004A-0000-1000-8000-0026BB765291"
    override val type = "public.hap.service.thermostat"
    override val characteristics = listOf(
            TargetTemperature(targetTemperatureInstanceId),
            CurrentTemperature(currentTemperatureInstanceId),
            Name(nameInstanceId))
    override val hidden = false
    override val primaryService = true
    override val linkedServices = emptyList<Int>()

}