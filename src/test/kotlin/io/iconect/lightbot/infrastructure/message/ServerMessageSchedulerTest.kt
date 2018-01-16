package io.iconect.lightbot.infrastructure.message

import io.iconect.lightbot.application.message.ServerMessageHandler
import io.iconect.lightbot.domain.message.ServerMessageFactory
import io.iconect.lightbot.domain.message.ServerMessageRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
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
        `when`(serverMessageRepository.retrieveMessages()).thenReturn(listOf(serverMessage1, serverMessage2))

        serverMessageScheduler.scheduleUnreadMessages()

        verify(serverMessageHandler).handleMessage(serverMessage1)
        verify(serverMessageHandler).handleMessage(serverMessage2)
    }
}