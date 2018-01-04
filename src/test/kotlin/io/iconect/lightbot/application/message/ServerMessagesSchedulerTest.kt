package io.iconect.lightbot.application.message

import io.iconect.lightbot.domain.message.ServerMessage
import io.iconect.lightbot.domain.message.ServerMessageRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
class ServerMessagesSchedulerTest {

    @Autowired
    private lateinit var serverMessagesScheduler: ServerMessagesScheduler

    @MockBean
    private lateinit var serverMessageRepositoryMock: ServerMessageRepository

    @Test
    fun `load unread messages and transform it to commands`() {
        `when`(serverMessageRepositoryMock.retrieveMessages())
                .thenReturn(listOf(
                        ServerMessage("room:kitchen-1:window:open", "test-channel-1"),
                        ServerMessage("room:kitchen-1:heater:degree:10", "test-channel-1")
                ))

        serverMessagesScheduler.handleUnreadMessages()
    }
}