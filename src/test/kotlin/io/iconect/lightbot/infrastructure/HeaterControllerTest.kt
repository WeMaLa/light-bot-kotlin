package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.application.Thermostat
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
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
class HeaterControllerTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @MockBean
    lateinit var thermostat: Thermostat

    @Test
    fun `adjust heater`() {
        val exchange = testRestTemplate.exchange("/api/heater/{identifier}", HttpMethod.PUT, HttpEntity(23), Void::class.java, "heater-identifier")

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        verify(thermostat).adjust("heater-identifier", 23)
    }
}