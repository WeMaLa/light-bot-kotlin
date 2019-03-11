package chat.to.lightbot.infrastructure

import chat.to.lightbot.domain.hap.VHabStatusRepository
import chat.to.server.bot.message.event.BotStatus
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@ActiveProfiles("unittest")
class CachedBotStatusRepositoryTest {

    @Autowired
    lateinit var repository: VHabStatusRepository

    @Autowired
    lateinit var applicationEventPublisher: ApplicationEventPublisher

    @MockBean
    lateinit var messagingTemplateMock: SimpMessagingTemplate

    @BeforeEach
    fun setUp() {
        repository.clear()
    }

    @Test
    fun `get initial vHab status`() {
        assertThat(repository.getStatus()).isEqualTo(BotStatus.STARTING)
    }

    @Test
    fun `update vHab status by direct call`() {
        repository.updateStatus(BotStatus.OK)
        assertThat(repository.getStatus()).isEqualTo(BotStatus.OK)
        verify(messagingTemplateMock).convertAndSend("/topic/status", BotStatus.OK.name)

        repository.updateStatus(BotStatus.AUTHENTICATION_FAILED)
        assertThat(repository.getStatus()).isEqualTo(BotStatus.AUTHENTICATION_FAILED)
        verify(messagingTemplateMock).convertAndSend("/topic/status", BotStatus.AUTHENTICATION_FAILED.name)
    }

    @Test
    fun `update vHab status by event`() {
        applicationEventPublisher.publishEvent(BotStatus.OK)
        assertThat(repository.getStatus()).isEqualTo(BotStatus.OK)
        verify(messagingTemplateMock).convertAndSend("/topic/status", BotStatus.OK.name)

        applicationEventPublisher.publishEvent(BotStatus.AUTHENTICATION_FAILED)
        assertThat(repository.getStatus()).isEqualTo(BotStatus.AUTHENTICATION_FAILED)
        verify(messagingTemplateMock).convertAndSend("/topic/status", BotStatus.AUTHENTICATION_FAILED.name)
    }

    @Test
    fun `verify websocket is called only when state is changed`() {
        applicationEventPublisher.publishEvent(BotStatus.OK)
        assertThat(repository.getStatus()).isEqualTo(BotStatus.OK)

        applicationEventPublisher.publishEvent(BotStatus.OK)
        assertThat(repository.getStatus()).isEqualTo(BotStatus.OK)

        verify(messagingTemplateMock, times(1)).convertAndSend("/topic/status", BotStatus.OK.name)
    }
}