package io.iconect.lightbot.infrastructure.message

import io.iconect.lightbot.TestLightBotApplication
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.*
import org.springframework.test.web.client.response.MockRestResponseCreators.*
import org.springframework.web.client.RestTemplate

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
@ContextConfiguration(classes = [TestLightBotApplication::class])
class ServerRegistrationExchangeServiceTest {

    @Autowired
    lateinit var serverRegistrationExchangeService: ServerRegistrationExchangeService

    @Autowired
    lateinit var restTemplate: RestTemplate

    lateinit var server: MockRestServiceServer

    @Before
    fun setUp() {
        server = MockRestServiceServer.bindTo(restTemplate).build()
    }

    @Test
    fun `register light bot on iconect server`() {
        server.expect(requestTo("http://server.unit.test/api/user"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath<String>("email", equalTo<String>("unit@test.bot")))
                .andExpect(jsonPath<String>("password", equalTo<String>("unit-test-bot-password")))
                .andExpect(jsonPath<String>("username", equalTo<String>("unit-test-bot-username")))
                .andRespond(withSuccess())

        assertThat(serverRegistrationExchangeService.registerBot()).isTrue()

        server.verify()
    }

    @Test
    fun `register light bot on iconect server and server responds bad request`() {
        server.expect(requestTo("http://server.unit.test/api/user"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath<String>("email", equalTo<String>("unit@test.bot")))
                .andExpect(jsonPath<String>("password", equalTo<String>("unit-test-bot-password")))
                .andExpect(jsonPath<String>("username", equalTo<String>("unit-test-bot-username")))
                .andRespond(withBadRequest())

        assertThat(serverRegistrationExchangeService.registerBot()).isFalse()

        server.verify()
    }

    @Test
    fun `register light bot on iconect server and server responds conflict`() {
        server.expect(requestTo("http://server.unit.test/api/user"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath<String>("email", equalTo<String>("unit@test.bot")))
                .andExpect(jsonPath<String>("password", equalTo<String>("unit-test-bot-password")))
                .andExpect(jsonPath<String>("username", equalTo<String>("unit-test-bot-username")))
                .andRespond(withStatus(HttpStatus.CONFLICT))

        assertThat(serverRegistrationExchangeService.registerBot()).isFalse()

        server.verify()
    }

}