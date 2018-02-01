package io.iconect.lightbot.application.hap

import io.iconect.lightbot.domain.hap.AccessoryFactory
import io.iconect.lightbot.domain.hap.AccessoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HapInitializer @Autowired constructor(private val accessoryRepository: AccessoryRepository, private val accessoryFactory: AccessoryFactory) {

    fun initialize() {
        initializeKitchen()
        initializeDiner()
    }

    private fun initializeKitchen() {
        val kitchenWindow = accessoryFactory.createWindowAccessory(11000, 11100, 11101, 11102, 11103, "Kitchen window")
        accessoryRepository.store(kitchenWindow)

        val kitchenLightBulb = accessoryFactory.createLightBulbAccessory(12000, 12100, 12101, 12102, "Kitchen light bulb")
        accessoryRepository.store(kitchenLightBulb)
    }

    private fun initializeDiner() {
        val dinerThermostat = accessoryFactory.createThermostatAccessory(21000, 21100, 21101, 21102, 21103, "Diner thermostat heater")
        accessoryRepository.store(dinerThermostat)
    }
}