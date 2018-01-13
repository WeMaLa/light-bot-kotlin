package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.hap.Accessory
import io.iconect.lightbot.domain.hap.AccessoryFactory
import io.iconect.lightbot.domain.hap.AccessoryRepository
import io.iconect.lightbot.infrastructure.model.AccessoriesDto
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
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
class AccessoryControllerTest {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    private lateinit var accessoryFactory: AccessoryFactory

    @MockBean
    private lateinit var accessoryRepository: AccessoryRepository

    @Test
    fun `find all accessories and map to dto`() {
        Mockito.`when`(accessoryRepository.findAll()).thenReturn(listOf(Accessory(1, emptyList())))

        val exchange = testRestTemplate.exchange("/api/accessories", HttpMethod.GET, HttpEntity.EMPTY, AccessoriesDto::class.java)

        val accessories = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(accessories.accessories)
                .extracting("aid", "services.size")
                .containsOnly(tuple(1, 0))
    }

    @Test
    fun `find all accessories and map to string`() {
        val accessory = accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy")
        Mockito.`when`(accessoryRepository.findAll()).thenReturn(listOf(accessory))

        val exchange = testRestTemplate.exchange("/api/accessories", HttpMethod.GET, HttpEntity.EMPTY, String::class.java)

        val accessories = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)

        assertThat(accessories).isEqualTo("{" +
                "\"accessories\":[" +
                "{\"aid\":1,\"services\":[" +
                "{\"iid\":1,\"type\":\"4A\",\"characteristics\":[" +
                "{\"iid\":2,\"type\":\"35\",\"value\":\"10.0\",\"format\":\"float\",\"perms\":[\"pr\",\"pw\",\"Notify\"]}," +
                "{\"iid\":3,\"type\":\"11\",\"value\":\"0.0\",\"format\":\"float\",\"perms\":[\"pr\",\"Notify\"]}," +
                "{\"iid\":4,\"type\":\"23\",\"value\":\"dummy\",\"format\":\"string\",\"perms\":[\"pr\"]}" +
                "]}" +
                "]}" +
                "]" +
                "}")
    }

}