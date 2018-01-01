package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.TestLightBotApplication
import io.iconect.lightbot.domain.VHabStatus
import io.iconect.lightbot.domain.VHabStatusRepository
import org.assertj.core.api.Assertions.assertThat
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
class CachedVHabStatusRepositoryTest {

    @Autowired
    lateinit var repository: VHabStatusRepository

    @Before
    fun setUp() {
        repository.clear()
    }

    @Test
    fun `get initial vHab status`() {
        assertThat(repository.getStatus()).isEqualTo(VHabStatus.STARTING)
    }

    @Test
    fun `update vHab status`() {
        repository.updateStatus(VHabStatus.OK)
        assertThat(repository.getStatus()).isEqualTo(VHabStatus.OK)

        repository.updateStatus(VHabStatus.AUTHENTICATION_FAILED)
        assertThat(repository.getStatus()).isEqualTo(VHabStatus.AUTHENTICATION_FAILED)
    }
}