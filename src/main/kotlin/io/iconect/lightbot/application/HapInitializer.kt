package io.iconect.lightbot.application

import io.iconect.lightbot.domain.hap.Accessory
import io.iconect.lightbot.domain.hap.service.Thermostat

class HapInitializer {

    companion object {
        fun initialize() : Accessory {
            val thermostat = Thermostat.Builder(2, 21, 22)
                    .currentTemperatureDescription("Kitchen heater")
                    .targetTemperatureDescription("Kitchen heater")
                    .build()
            return Accessory(1, listOf(thermostat))
        }
    }

}