package io.iconect.lightbot.application

import io.iconect.lightbot.domain.hap.Accessory
import io.iconect.lightbot.domain.hap.service.Thermostat

class HapInitializer {

    companion object {
        fun initialize(): Accessory {
            val thermostat = Thermostat(2, 21, 22, 23)
            return Accessory(1, listOf(thermostat))
        }
    }

}