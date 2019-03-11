package chat.to.lightbot.infrastructure.message

import chat.to.lightbot.application.message.ServerMessageHandler
import chat.to.lightbot.domain.message.ServerMessageFactory
import chat.to.lightbot.domain.message.ServerMessageRepository
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("unittest")
class ServerMessageSchedulerTest {

    @Autowired
    private lateinit var serverMessageScheduler: ServerMessageScheduler

    @Autowired
    private lateinit var serverMessageFactory: ServerMessageFactory

    @MockBean
    private lateinit var serverMessageRepository: ServerMessageRepository

    @MockBean
    private lateinit var serverMessageHandler: ServerMessageHandler

    @Test
    fun `verify each message is handled`() {
        val serverMessage1 = serverMessageFactory.createServerMessage("unit-test-message-1", "unit-test-channel")
        val serverMessage2 = serverMessageFactory.createServerMessage("unit-test-message-2", "unit-test-channel")
        whenever(serverMessageRepository.retrieveMessages()).thenReturn(listOf(serverMessage1, serverMessage2))

        serverMessageScheduler.scheduleUnreadMessages()

        verify(serverMessageHandler).handleMessage(serverMessage1)
        verify(serverMessageHandler).handleMessage(serverMessage2)
    }
}