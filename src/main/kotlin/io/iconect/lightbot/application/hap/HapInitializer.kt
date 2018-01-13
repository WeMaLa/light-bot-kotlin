package io.iconect.lightbot.application.hap

import io.iconect.lightbot.domain.hap.AccessoryFactory
import io.iconect.lightbot.domain.hap.AccessoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HapInitializer @Autowired constructor(private val accessoryRepository: AccessoryRepository, private val accessoryFactory: AccessoryFactory) {

    fun initialize() {
        val kitchenThermostat = accessoryFactory.createThermostatAccessory(10000, 10100, 10101, 10102, 10103, "Kitchen thermostat heater")
        accessoryRepository.store(kitchenThermostat)

        val kitchenWindow = accessoryFactory.createWindowAccessory(11000, 11100, 11101, 11102, 11103, "Kitchen window")
        accessoryRepository.store(kitchenWindow)

        val kitchenLightBulb = accessoryFactory.createLightBulbAccessory(12000, 12100, 12101, 12102, "Kitchen light bulb")
        accessoryRepository.store(kitchenLightBulb)
    }
}