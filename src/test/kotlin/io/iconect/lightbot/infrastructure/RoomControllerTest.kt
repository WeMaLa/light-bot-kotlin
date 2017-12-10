package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import io.iconect.lightbot.infrastructure.model.RoomDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unittest")
class RoomControllerTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @MockBean
    lateinit var roomRepository: RoomRepository

    @Test
    fun `check load all rooms`() {
        Mockito.`when`(roomRepository.loadAll()).thenReturn(listOf(
                Room.Builder("room-identifier-1").build(),
                Room.Builder("room-identifier-2").build()
        ))

        val exchange = testRestTemplate.exchange<List<RoomDto>>("/api/rooms", HttpMethod.GET, HttpEntity.EMPTY, object : ParameterizedTypeReference<List<RoomDto>>() {
        })

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK);
    }

}