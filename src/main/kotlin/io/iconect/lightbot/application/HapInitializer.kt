package io.iconect.lightbot.application

import io.iconect.lightbot.domain.Accessory
import io.iconect.lightbot.domain.service.Thermostat
import io.iconect.lightbot.domain.service.characteristic.Name

class HapInitializer {

    companion object {
        fun initialize(): Accessory {
            val kitchenThermostat = Thermostat(2, 21, 22, 23)
            (kitchenThermostat.characteristics.first { c -> c is Name } as Name).updateName("Kitchen thermostat heater")
            return Accessory(1, listOf(kitchenThermostat))
        }
    }

}