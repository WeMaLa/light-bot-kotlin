package chat.to.lightbot.infrastructure

import chat.to.lightbot.domain.hap.Accessory
import chat.to.lightbot.domain.hap.AccessoryFactory
import chat.to.lightbot.domain.hap.AccessoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
class CachedAccessoryRepositoryTest {

    @Autowired
    lateinit var repository: AccessoryRepository

    @Autowired
    private lateinit var accessoryFactory: AccessoryFactory

    @Before
    fun setUp() {
        repository.clear()
    }

    @Test
    fun `find all accessories but repository is empty`() {
        assertThat(repository.findAll()).isEmpty()
    }

    @Test
    fun `store accessory`() {
        val accessory = accessoryFactory.createThermostatAccessory(1, 2, 21, 22, 23, "dummy")

        assertThat(repository.findAll()).isEmpty()

        repository.store(accessory)

        assertThat(repository.findAll()).containsExactly(accessory)
    }

    @Test
    fun `update stored room`() {
        val accessory = accessoryFactory.createThermostatAccessory(1, 2, 21, 22, 23, "dummy")

        repository.store(accessory)
        assertThat(repository.findAll()).containsExactly(accessory)

        val updatedAccessory = Accessory(1, emptyList())

        repository.store(updatedAccessory)
        assertThat(repository.findAll()).containsExactly(updatedAccessory)
    }

    @Test
    fun `find by instance id (aid)`() {
        val accessory = accessoryFactory.createThermostatAccessory(1, 2, 21, 22, 23, "dummy")

        repository.store(accessory)
        assertThat(repository.findAll()).containsExactly(accessory)

        assertThat(repository.findByInstanceId(1)).isEqualTo(accessory)
        assertThat(repository.findByInstanceId(2)).isNull()
    }

}