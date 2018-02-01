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
        val window = accessoryFactory.createWindowAccessory(11000, 11100, 11101, 11102, 11103, "Fenster")
        accessoryRepository.store(window)

        val lightBulb = accessoryFactory.createLightBulbAccessory(12000, 12100, 12101, 12102, "Lampe")
        accessoryRepository.store(lightBulb)
    }

    private fun initializeDiner() {
        val thermostat = accessoryFactory.createThermostatAccessory(21000, 21100, 21101, 21102, 21103, "Heizung")
        accessoryRepository.store(thermostat)

        val window = accessoryFactory.createWindowAccessory(22000, 22100, 22101, 22102, 22103, "Fenster")
        accessoryRepository.store(window)

        val lightBulb1 = accessoryFactory.createLightBulbAccessory(23000, 23100, 23101, 23102, "Lampe")
        accessoryRepository.store(lightBulb1)

        val lightBulb2 = accessoryFactory.createLightBulbAccessory(24000, 24100, 24101, 24102, "Lampe")
        accessoryRepository.store(lightBulb2)
    }
}