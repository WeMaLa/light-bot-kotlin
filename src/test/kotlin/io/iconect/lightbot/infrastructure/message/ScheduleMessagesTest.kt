package io.iconect.lightbot.infrastructure.message

import io.iconect.lightbot.TestLightBotApplication
import io.iconect.lightbot.infrastructure.message.model.ServerMessage
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
@ContextConfiguration(classes = [TestLightBotApplication::class])
class ScheduleMessagesTest {

    @Autowired
    private lateinit var scheduleMessages: ScheduleMessages

    @MockBean
    private lateinit var serverMessageExchangeService: ServerMessageExchangeService

    @Test
    fun `load unread messages and transform it to commands`() {
        `when`(serverMessageExchangeService.retrieveMessages())
                .thenReturn(listOf(
                        ServerMessage("room:kitchen-1:window:open", "test-channel-1"),
                        ServerMessage("room:kitchen-1:heater:degree:10", "test-channel-1")
                ))

        scheduleMessages.handleUnreadMessages()
    }
}