package io.iconect.lightbot.domain.hap.service

import io.iconect.lightbot.domain.hap.service.characteristic.CurrentTemperature
import io.iconect.lightbot.domain.hap.service.characteristic.TargetTemperature

// service page 220
data class Thermostat(override val instanceId: Int,
                      private val targetTemperatureInstanceId: Int,
                      private val targetTemperatureDescription: String?,
                      private val currentTemperatureInstanceId: Int,
                      private val currentTemperatureDescription: String?) : Service {

    val uuid = "0000004A-0000-1000-8000-0026BB765291"
    override val type = "public.hap.service.thermostat"
    override val characteristics = listOf(TargetTemperature(targetTemperatureInstanceId, description = targetTemperatureDescription), CurrentTemperature(currentTemperatureInstanceId, description = currentTemperatureDescription)) // TODO
    override val hidden = false
    override val primaryService = true
    override val linkedServices = emptyList<Int>()

    class Builder(private val instanceId: Int, private val targetTemperatureInstanceId: Int, private val currentTemperatureInstanceId: Int) {
        private var targetTemperatureDescription: String? = null
        private var currentTemperatureDescription: String? = null

        fun targetTemperatureDescription(targetTemperatureDescription: String): Builder {
            this.targetTemperatureDescription = targetTemperatureDescription
            return this
        }

        fun currentTemperatureDescription(currentTemperatureDescription: String): Builder {
            this.currentTemperatureDescription = currentTemperatureDescription
            return this
        }

        fun build(): Thermostat {
            return Thermostat(instanceId, targetTemperatureInstanceId, targetTemperatureDescription, currentTemperatureInstanceId, currentTemperatureDescription)
        }
    }
}