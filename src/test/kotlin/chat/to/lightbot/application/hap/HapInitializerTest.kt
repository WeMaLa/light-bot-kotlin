package chat.to.lightbot.application.hap

import chat.to.lightbot.domain.hap.AccessoryRepository
import chat.to.lightbot.domain.hap.service.LightBulb
import chat.to.lightbot.domain.hap.service.Thermostat
import chat.to.lightbot.domain.hap.service.Window
import chat.to.lightbot.domain.hap.service.characteristic.*
import io.iconect.lightbot.domain.hap.service.characteristic.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
class HapInitializerTest {

    @Autowired
    private lateinit var accessoryRepository: AccessoryRepository

    @Autowired
    private lateinit var hapInitializer: HapInitializer

    @Test
    fun `verify kitchen initialization`() {
        hapInitializer.initialize()

        verifyWindow(11000, 11100, 11101, 11102, 11103, "kitchen window")
        verifyLightBulb(12000, 12100, 12101, 12102, "kitchen lamp")
    }

    @Test
    fun `verify diner initialization`() {
        hapInitializer.initialize()

        verifyHeater(21000, 21100, 21101, 21102, 21103, "diner heater")
        verifyWindow(22000, 22100, 22101, 22102, 22103, "diner window")
        verifyLightBulb(23000, 23100, 23101, 23102, "diner lamp 1")
        verifyLightBulb(24000, 24100, 24101, 24102, "diner lamp 2")
    }

    @Test
    fun `verify living initialization`() {
        hapInitializer.initialize()

        verifyHeater(31000, 31100, 31101, 31102, 31103, "living heater")
        verifyWindow(32000, 32100, 32101, 32102, 32103, "living window")
        verifyLightBulb(33000, 33100, 33101, 33102, "living lamp 1")
        verifyLightBulb(34000, 34100, 34101, 34102, "living lamp 2")
        verifyLightBulb(35000, 35100, 35101, 35102, "living lamp 3")
        verifyLightBulb(36000, 36100, 36101, 36102, "living lamp 4")
    }

    @Test
    fun `verify store initialization`() {
        hapInitializer.initialize()

        verifyLightBulb(41000, 41100, 41101, 41102, "store lamp")
    }

    @Test
    fun `verify rest 1 initialization`() {
        hapInitializer.initialize()

        verifyLightBulb(51000, 51100, 51101, 51102, "rest (small) lamp")
    }

    @Test
    fun `verify rest 2 initialization`() {
        hapInitializer.initialize()

        verifyLightBulb(61000, 61100, 61101, 61102, "rest (big) lamp")
    }

    @Test
    fun `verify entrance initialization`() {
        hapInitializer.initialize()

        verifyLightBulb(71000, 71100, 71101, 71102, "entrance lamp 1")
        verifyLightBulb(72000, 72100, 72101, 72102, "entrance lamp 2")
    }

    @Test
    fun `verify bed initialization`() {
        hapInitializer.initialize()

        verifyHeater(81000, 81100, 81101, 81102, 81103, "bed heater")
        verifyWindow(82000, 82100, 82101, 82102, 82103, "bed window")
        verifyLightBulb(83000, 83100, 83101, 83102, "bed lamp 1")
        verifyLightBulb(84000, 84100, 84101, 84102, "bed lamp 2")
    }

    @Test
    fun `verify study initialization`() {
        hapInitializer.initialize()

        verifyHeater(91000, 91100, 91101, 91102, 91103, "study heater 1")
        verifyHeater(92000, 92100, 92101, 92102, 92103, "study heater 2")
        verifyWindow(93000, 93100, 93101, 93102, 93103, "study window 1")
        verifyWindow(94000, 94100, 94101, 94102, 94103, "study window 2")
        verifyLightBulb(95000, 95100, 95101, 95102, "study lamp 1")
        verifyLightBulb(96000, 96100, 96101, 96102, "study lamp 2")
    }

    private fun verifyHeater(accessoryId: Int, serviceId: Int, targetTemperatureId: Int, currentTemperatureId: Int, nameId: Int, nameValue: String) {
        val accessory = accessoryRepository.findByInstanceId(accessoryId)!!

        assertThat(accessory).isNotNull
        assertThat(accessory.instanceId).isEqualTo(accessoryId)
        assertThat(accessory.services.filter { s -> s is Thermostat }.size).isEqualTo(1)

        val thermostat = accessory.services.first { s -> s is Thermostat } as Thermostat
        assertThat(thermostat.instanceId).isEqualTo(serviceId)

        val targetTemperature = thermostat.characteristics.first { c -> c is TargetTemperature } as TargetTemperature
        assertThat(targetTemperature.instanceId).isEqualTo(targetTemperatureId)
        assertThat(targetTemperature.accessoryInstanceId).isEqualTo(accessoryId)
        assertThat(targetTemperature.value).isEqualTo("10.0")

        val currentTemperature = thermostat.characteristics.first { c -> c is CurrentTemperature } as CurrentTemperature
        assertThat(currentTemperature.instanceId).isEqualTo(currentTemperatureId)
        assertThat(currentTemperature.accessoryInstanceId).isEqualTo(accessoryId)
        assertThat(currentTemperature.value).isEqualTo("0.0")

        val name = thermostat.characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(nameId)
        assertThat(name.accessoryInstanceId).isEqualTo(accessoryId)
        assertThat(name.value).isEqualTo(nameValue)
    }

    private fun verifyWindow(accessoryId: Int, serviceId: Int, targetPositionId: Int, currentPositionId: Int, nameId: Int, nameValue: String) {
        val accessory = accessoryRepository.findByInstanceId(accessoryId)!!

        assertThat(accessory).isNotNull
        assertThat(accessory.instanceId).isEqualTo(accessoryId)
        assertThat(accessory.services.filter { s -> s is Window }.size).isEqualTo(1)

        val window = accessory.services.first { s -> s is Window } as Window
        assertThat(window.instanceId).isEqualTo(serviceId)

        val targetPosition = window.characteristics.first { c -> c is TargetPosition } as TargetPosition
        assertThat(targetPosition.instanceId).isEqualTo(targetPositionId)
        assertThat(targetPosition.accessoryInstanceId).isEqualTo(accessoryId)
        assertThat(targetPosition.value).isEqualTo("0")

        val currentPosition = window.characteristics.first { c -> c is CurrentPosition } as CurrentPosition
        assertThat(currentPosition.instanceId).isEqualTo(currentPositionId)
        assertThat(currentPosition.accessoryInstanceId).isEqualTo(accessoryId)
        assertThat(currentPosition.value).isEqualTo("0")

        val name = window.characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(nameId)
        assertThat(name.accessoryInstanceId).isEqualTo(accessoryId)
        assertThat(name.value).isEqualTo(nameValue)
    }

    private fun verifyLightBulb(accessoryId: Int, serviceId: Int, onId: Int, nameId: Int, nameValue: String) {
        val accessory = accessoryRepository.findByInstanceId(accessoryId)!!

        assertThat(accessory).isNotNull
        assertThat(accessory.instanceId).isEqualTo(accessoryId)
        assertThat(accessory.services.filter { s -> s is LightBulb }.size).isEqualTo(1)

        val lightbulb = accessory.services.first { s -> s is LightBulb } as LightBulb
        assertThat(lightbulb.instanceId).isEqualTo(serviceId)
        assertThat(lightbulb.accessoryInstanceId).isEqualTo(accessoryId)

        val currentPosition = lightbulb.characteristics.first { c -> c is On } as On
        assertThat(currentPosition.instanceId).isEqualTo(onId)
        assertThat(currentPosition.accessoryInstanceId).isEqualTo(accessoryId)
        assertThat(currentPosition.value).isEqualTo("off")

        val name = lightbulb.characteristics.first { c -> c is Name } as Name
        assertThat(name.instanceId).isEqualTo(nameId)
        assertThat(name.accessoryInstanceId).isEqualTo(accessoryId)
        assertThat(name.value).isEqualTo(nameValue)
    }

}