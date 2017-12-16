package io.iconect.lightbot.application

import io.iconect.lightbot.domain.Accessory
import io.iconect.lightbot.domain.AccessoryRepository
import io.iconect.lightbot.domain.service.Thermostat
import io.iconect.lightbot.domain.service.characteristic.Name
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HapInitializer @Autowired constructor(private val accessoryRepository: AccessoryRepository) {

    fun initialize() {
        val kitchenThermostat = Thermostat(2, 21, 22, 23)
        (kitchenThermostat.characteristics.first { c -> c is Name } as Name).updateName("Kitchen thermostat heater")
        accessoryRepository.store(Accessory(1, listOf(kitchenThermostat)))
    }

}