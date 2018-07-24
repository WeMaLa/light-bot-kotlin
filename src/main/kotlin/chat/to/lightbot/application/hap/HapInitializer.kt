package chat.to.lightbot.application.hap

import chat.to.lightbot.domain.hap.AccessoryFactory
import chat.to.lightbot.domain.hap.AccessoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HapInitializer @Autowired constructor(private val accessoryRepository: AccessoryRepository, private val accessoryFactory: AccessoryFactory) {

    fun initialize() {
        initializeKitchen()
        initializeDiner()
        initializeLiving()
        initializeStore()
        initializeRestSmall()
        initializeRestBig()
        initializeEntrance()
        initializeBed()
        initializeStudy()
    }

    private fun initializeKitchen() {
        val window = accessoryFactory.createWindowAccessory(11000, 11100, 11101, 11102, 11103, "kitchen window")
        accessoryRepository.store(window)

        val lightBulb = accessoryFactory.createLightBulbAccessory(12000, 12100, 12101, 12102, "kitchen lamp")
        accessoryRepository.store(lightBulb)
    }

    private fun initializeDiner() {
        val thermostat = accessoryFactory.createThermostatAccessory(21000, 21100, 21101, 21102, 21103, "diner heater")
        accessoryRepository.store(thermostat)

        val window = accessoryFactory.createWindowAccessory(22000, 22100, 22101, 22102, 22103, "diner window")
        accessoryRepository.store(window)

        val lightBulb1 = accessoryFactory.createLightBulbAccessory(23000, 23100, 23101, 23102, "diner lamp 1")
        accessoryRepository.store(lightBulb1)
        val lightBulb2 = accessoryFactory.createLightBulbAccessory(24000, 24100, 24101, 24102, "diner lamp 2")
        accessoryRepository.store(lightBulb2)
    }

    private fun initializeLiving() {
        val thermostat = accessoryFactory.createThermostatAccessory(31000, 31100, 31101, 31102, 31103, "living heater")
        accessoryRepository.store(thermostat)

        val window = accessoryFactory.createWindowAccessory(32000, 32100, 32101, 32102, 32103, "living window")
        accessoryRepository.store(window)

        val lightBulb1 = accessoryFactory.createLightBulbAccessory(33000, 33100, 33101, 33102, "living lamp 1")
        accessoryRepository.store(lightBulb1)
        val lightBulb2 = accessoryFactory.createLightBulbAccessory(34000, 34100, 34101, 34102, "living lamp 2")
        accessoryRepository.store(lightBulb2)
        val lightBulb3 = accessoryFactory.createLightBulbAccessory(35000, 35100, 35101, 35102, "living lamp 3")
        accessoryRepository.store(lightBulb3)
        val lightBulb4 = accessoryFactory.createLightBulbAccessory(36000, 36100, 36101, 36102, "living lamp 4")
        accessoryRepository.store(lightBulb4)
    }

    private fun initializeStore() {
        val lightBulb = accessoryFactory.createLightBulbAccessory(41000, 41100, 41101, 41102, "store lamp")
        accessoryRepository.store(lightBulb)
    }

    private fun initializeRestSmall() {
        val lightBulb = accessoryFactory.createLightBulbAccessory(51000, 51100, 51101, 51102, "rest (small) lamp")
        accessoryRepository.store(lightBulb)
    }

    private fun initializeRestBig() {
        val lightBulb = accessoryFactory.createLightBulbAccessory(61000, 61100, 61101, 61102, "rest (big) lamp")
        accessoryRepository.store(lightBulb)
    }

    private fun initializeEntrance() {
        val lightBulb1 = accessoryFactory.createLightBulbAccessory(71000, 71100, 71101, 71102, "entrance lamp 1")
        accessoryRepository.store(lightBulb1)
        val lightBulb2 = accessoryFactory.createLightBulbAccessory(72000, 72100, 72101, 72102, "entrance lamp 2")
        accessoryRepository.store(lightBulb2)
    }

    private fun initializeBed() {
        val thermostat = accessoryFactory.createThermostatAccessory(81000, 81100, 81101, 81102, 81103, "bed heater")
        accessoryRepository.store(thermostat)

        val window = accessoryFactory.createWindowAccessory(82000, 82100, 82101, 82102, 82103, "bed window")
        accessoryRepository.store(window)

        val lightBulb1 = accessoryFactory.createLightBulbAccessory(83000, 83100, 83101, 83102, "bed lamp 1")
        accessoryRepository.store(lightBulb1)
        val lightBulb2 = accessoryFactory.createLightBulbAccessory(84000, 84100, 84101, 84102, "bed lamp 2")
        accessoryRepository.store(lightBulb2)
    }

    private fun initializeStudy() {
        val thermostat1 = accessoryFactory.createThermostatAccessory(91000, 91100, 91101, 91102, 91103, "study heater 1")
        accessoryRepository.store(thermostat1)
        val thermostat2 = accessoryFactory.createThermostatAccessory(92000, 92100, 92101, 92102, 92103, "study heater 2")
        accessoryRepository.store(thermostat2)

        val window1 = accessoryFactory.createWindowAccessory(93000, 93100, 93101, 93102, 93103, "study window 1")
        accessoryRepository.store(window1)
        val window2 = accessoryFactory.createWindowAccessory(94000, 94100, 94101, 94102, 94103, "study window 2")
        accessoryRepository.store(window2)

        val lightBulb1 = accessoryFactory.createLightBulbAccessory(95000, 95100, 95101, 95102, "study lamp 1")
        accessoryRepository.store(lightBulb1)
        val lightBulb2 = accessoryFactory.createLightBulbAccessory(96000, 96100, 96101, 96102, "study lamp 2")
        accessoryRepository.store(lightBulb2)
    }
}