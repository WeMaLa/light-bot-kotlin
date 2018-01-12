package io.iconect.lightbot.application.hap

import io.iconect.lightbot.domain.hap.Accessory
import io.iconect.lightbot.domain.hap.AccessoryRepository
import io.iconect.lightbot.domain.hap.service.Thermostat
import io.iconect.lightbot.domain.hap.service.Window
import io.iconect.lightbot.domain.hap.service.characteristic.Name
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HapInitializer @Autowired constructor(private val accessoryRepository: AccessoryRepository) {

    fun initialize() {
        val kitchenThermostat = Thermostat(10100, 10101, 10102, 10103)
        (kitchenThermostat.characteristics.first { c -> c is Name } as Name).updateName("Kitchen thermostat heater")
        accessoryRepository.store(Accessory(10000, listOf(kitchenThermostat)))

        val kitchenWindow = Window(11100, 11101, 11102, 11103)
        (kitchenWindow.characteristics.first { c -> c is Name } as Name).updateName("Kitchen window")
        accessoryRepository.store(Accessory(11000, listOf(kitchenWindow)))
    }
}