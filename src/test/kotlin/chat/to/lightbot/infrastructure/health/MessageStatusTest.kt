package chat.to.lightbot.infrastructure.health

import chat.to.lightbot.domain.hap.VHabStatusRepository
import chat.to.server.bot.message.event.BotStatus
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unittest")
class MessageStatusTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @MockBean
    lateinit var vHabStatusRepositoryMock: VHabStatusRepository

    @Test
    fun `get actual vHab status`() {
        whenever(vHabStatusRepositoryMock.getStatus()).thenReturn(BotStatus.RECEIVE_MESSAGES_FAILED)

        val exchange = testRestTemplate.exchange("/actuator/health", HttpMethod.GET, HttpEntity.EMPTY, String::class.java)

        val status = exchange.body!!

       assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
       assertThat(status).contains("{\"messageStatus\":{\"status\":\"RECEIVE_MESSAGES_FAILED\"}")
    }

}