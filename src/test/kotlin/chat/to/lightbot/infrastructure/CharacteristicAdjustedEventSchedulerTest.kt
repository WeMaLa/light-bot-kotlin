package chat.to.lightbot.infrastructure

import chat.to.lightbot.application.hap.HapEventHandler
import chat.to.lightbot.domain.hap.CharacteristicAdjustedEvent
import chat.to.lightbot.domain.hap.CharacteristicAdjustedEventRepository
import chat.to.lightbot.infrastructure.CachedCharacteristicAdjustedEventRepository
import chat.to.lightbot.infrastructure.CharacteristicAdjustedEventScheduler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner


@SpringBootTest
@ActiveProfiles("unittest")
class CharacteristicAdjustedEventSchedulerTest {

    private lateinit var scheduler: CharacteristicAdjustedEventScheduler

    private lateinit var characteristicAdjustedEventRepository: CharacteristicAdjustedEventRepository

    @MockBean
    private lateinit var messagingTemplateMock: SimpMessagingTemplate

    @MockBean
    private lateinit var hapEventHandlerMock: HapEventHandler

    @BeforeEach
    fun setUp() {
        characteristicAdjustedEventRepository = CachedCharacteristicAdjustedEventRepository()
        scheduler = CharacteristicAdjustedEventScheduler(messagingTemplateMock, characteristicAdjustedEventRepository, hapEventHandlerMock)
    }

    @Test
    fun `verify web socket event is thrown when event exists`() {
        val event = CharacteristicAdjustedEvent(1, 2, "unit-test-event")
        characteristicAdjustedEventRepository.pushEvent(event)

        scheduler.scheduleEvents()

        verify(messagingTemplateMock).convertAndSend("/topic/event", "{\"accessoryId\":1,\"characteristicId\":2,\"value\":\"unit-test-event\"}")
        verify(hapEventHandlerMock).handleEvent(event)
    }

    @Test
    fun `verify web socket event not thrown when not event exists`() {
        scheduler.scheduleEvents()

        verifyNoMoreInteractions(messagingTemplateMock)
        verifyNoMoreInteractions(hapEventHandlerMock)
    }
}