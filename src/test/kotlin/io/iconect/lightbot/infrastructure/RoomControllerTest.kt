package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import io.iconect.lightbot.infrastructure.model.RoomDto
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
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
    fun `find all rooms`() {
        Mockito.`when`(roomRepository.findAll()).thenReturn(listOf(
                Room.Builder("room-identifier-1").designation("room-designation-1").build(),
                Room.Builder("room-identifier-2").designation("room-designation-2").build()
        ))

        val exchange = testRestTemplate.exchange<List<RoomDto>>("/api/rooms", HttpMethod.GET, HttpEntity.EMPTY, object : ParameterizedTypeReference<List<RoomDto>>() {
        })

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(exchange.body)
                .extracting("identifier", "designation")
                .containsOnly(
                        tuple("room-identifier-1", "room-designation-1"),
                        tuple("room-identifier-2", "room-designation-2")
                )
    }

    @Test
    fun `find room by identifier`() {
        Mockito.`when`(roomRepository.find("room-identifier"))
                .thenReturn(Room.Builder("room-identifier").build())

        val exchange = testRestTemplate.exchange<RoomDto>("/api/room/{identifier}", HttpMethod.GET, HttpEntity.EMPTY, RoomDto::class.java, "room-identifier")

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(exchange.body?.identifier).isEqualTo("room-identifier")
    }

    @Test
    fun `find room by identifier with room not found`() {
        val exchange = testRestTemplate.exchange<String>("/api/room/{identifier}", HttpMethod.GET, HttpEntity.EMPTY, String::class.java, "room-not-found")

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(exchange.body).isEqualTo("{\"message\":\"Room 'room-not-found' not found.\"}")
    }

}