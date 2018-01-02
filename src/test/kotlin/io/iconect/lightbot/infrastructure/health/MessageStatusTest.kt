package io.iconect.lightbot.infrastructure.health

import io.iconect.lightbot.domain.VHabStatus
import io.iconect.lightbot.domain.VHabStatusRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unittest")
class MessageStatusTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @MockBean
    lateinit var vHabStatusRepositoryMock: VHabStatusRepository

    @Test
    fun `get actual vHab status`() {
        Mockito.`when`(vHabStatusRepositoryMock.getStatus()).thenReturn(VHabStatus.RECEIVE_MESSAGES_FAILED)

        val exchange = testRestTemplate.exchange("/actuator/health", HttpMethod.GET, HttpEntity.EMPTY, String::class.java)

        val status = exchange.body!!

       assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
       assertThat(status).contains("{\"messageStatus\":{\"status\":\"RECEIVE_MESSAGES_FAILED\"}")
    }

}