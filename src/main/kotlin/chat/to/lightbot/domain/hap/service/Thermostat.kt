package chat.to.lightbot.domain.hap.service

import chat.to.lightbot.domain.hap.service.characteristic.CurrentTemperature
import chat.to.lightbot.domain.hap.service.characteristic.Name
import chat.to.lightbot.domain.hap.service.characteristic.TargetTemperature

// service page 220
data class Thermostat(override val instanceId: Int,
                      override val accessoryInstanceId: Int,
                      private val targetTemperatureInstanceId: Int,
                      private val currentTemperatureInstanceId: Int,
                      private val nameInstanceId: Int,
                      private val eventPublisher: (accessoryInstanceId: Int, characteristicInstanceId: Int, value: String) -> Unit) : Service {

    override val uuid = "0000004A-0000-1000-8000-0026BB765291"
    override val type = "public.hap.service.thermostat"
    override val characteristics = listOf(
            TargetTemperature(targetTemperatureInstanceId, accessoryInstanceId, eventPublisher),
            CurrentTemperature(currentTemperatureInstanceId, accessoryInstanceId, eventPublisher),
            Name(nameInstanceId, accessoryInstanceId, eventPublisher))
    override val hidden = false
    override val primaryService = true
    override val linkedServices = emptyList<Int>()

}