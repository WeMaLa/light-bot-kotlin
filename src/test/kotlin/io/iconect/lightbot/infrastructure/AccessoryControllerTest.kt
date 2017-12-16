package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.TestLightBotApplication
import io.iconect.lightbot.domain.Accessory
import io.iconect.lightbot.domain.AccessoryRepository
import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import io.iconect.lightbot.infrastructure.model.AccessoryDto
import io.iconect.lightbot.infrastructure.model.RoomDto
import org.assertj.core.api.Assertions
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
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unittest")
@ContextConfiguration(classes = [TestLightBotApplication::class])
class AccessoryControllerTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @MockBean
    lateinit var accessoryRepository: AccessoryRepository

    @Test
    fun `find all accessories`() {
        Mockito.`when`(accessoryRepository.findAll()).thenReturn(listOf(
                Accessory(1, emptyList()),
                Accessory(2, emptyList())
        ))

        val exchange = testRestTemplate.exchange("/api/accessories", HttpMethod.GET, HttpEntity.EMPTY, object : ParameterizedTypeReference<List<AccessoryDto>>() {
        })

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(exchange.body)
                .extracting("aid")
                .containsOnly(1, 2)
    }

}