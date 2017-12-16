package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.TestLightBotApplication
import io.iconect.lightbot.domain.Accessory
import io.iconect.lightbot.domain.AccessoryRepository
import io.iconect.lightbot.domain.service.Thermostat
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
@ContextConfiguration(classes = [TestLightBotApplication::class])
class CachedAccessoryRepositoryTest {

    @Autowired
    lateinit var accessoryRepository: AccessoryRepository

    @Before
    fun setUp() {
        accessoryRepository.clear()
    }

    @Test
    fun `find all accessories but repository is empty`() {
        Assertions.assertThat(accessoryRepository.findAll()).isEmpty()
    }

    @Test
    fun `store accessory`() {
        val accessory = Accessory(1, listOf(Thermostat(2, 21, 22, 23)))

        Assertions.assertThat(accessoryRepository.findAll()).isEmpty()

        accessoryRepository.store(accessory)

        Assertions.assertThat(accessoryRepository.findAll()).containsExactly(accessory)
    }

    @Test
    fun `update stored room`() {
        val accessory = Accessory(1, listOf(Thermostat(2, 21, 22, 23)))

        accessoryRepository.store(accessory)
        Assertions.assertThat(accessoryRepository.findAll()).containsExactly(accessory)

        val updatedAccessory = Accessory(1, emptyList())

        accessoryRepository.store(updatedAccessory)
        Assertions.assertThat(accessoryRepository.findAll()).containsExactly(updatedAccessory)
    }

}