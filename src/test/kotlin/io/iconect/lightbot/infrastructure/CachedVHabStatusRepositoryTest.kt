package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.VHabStatus
import io.iconect.lightbot.domain.VHabStatusRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
class CachedVHabStatusRepositoryTest {

    @Autowired
    lateinit var repository: VHabStatusRepository

    @Autowired
    lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Before
    fun setUp() {
        repository.clear()
    }

    @Test
    fun `get initial vHab status`() {
        assertThat(repository.getStatus()).isEqualTo(VHabStatus.STARTING)
    }

    @Test
    fun `update vHab status by direct call`() {
        repository.updateStatus(VHabStatus.OK)
        assertThat(repository.getStatus()).isEqualTo(VHabStatus.OK)

        repository.updateStatus(VHabStatus.AUTHENTICATION_FAILED)
        assertThat(repository.getStatus()).isEqualTo(VHabStatus.AUTHENTICATION_FAILED)
    }

    @Test
    fun `update vHab status by event`() {
        applicationEventPublisher.publishEvent(VHabStatus.OK)
        assertThat(repository.getStatus()).isEqualTo(VHabStatus.OK)

        applicationEventPublisher.publishEvent(VHabStatus.AUTHENTICATION_FAILED)
        assertThat(repository.getStatus()).isEqualTo(VHabStatus.AUTHENTICATION_FAILED)
    }
}