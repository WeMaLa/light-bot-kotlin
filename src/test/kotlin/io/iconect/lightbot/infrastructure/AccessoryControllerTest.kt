package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.TestLightBotApplication
import io.iconect.lightbot.domain.Accessory
import io.iconect.lightbot.domain.AccessoryRepository
import io.iconect.lightbot.domain.service.Thermostat
import io.iconect.lightbot.infrastructure.model.AccessoriesDto
import org.apache.coyote.http11.Constants.a
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.assertj.core.groups.Tuple
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
                Accessory(1, listOf(Thermostat(11, 12, 13, 14))),
                Accessory(2, emptyList())
        ))

        val exchange = testRestTemplate.exchange("/api/accessories", HttpMethod.GET, HttpEntity.EMPTY, AccessoriesDto::class.java)

        val accessories = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(accessories.accessories)
                .extracting("aid", "services.size")
                .containsOnly(
                        tuple(1, 1),
                        tuple(2, 0))

        val services = accessories.accessories.flatMap { a -> a.services }

        assertThat(services)
                .extracting("type", "iid")
                .containsExactly(
                        tuple("4A", 11)
                )
    }

}