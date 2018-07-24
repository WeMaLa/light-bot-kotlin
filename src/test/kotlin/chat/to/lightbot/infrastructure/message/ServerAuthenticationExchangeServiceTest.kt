package chat.to.lightbot.infrastructure.message

import chat.to.lightbot.domain.hap.VHabStatus
import chat.to.lightbot.domain.hap.VHabStatusRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators.*
import org.springframework.web.client.RestTemplate

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
class ServerAuthenticationExchangeServiceTest {

    @Autowired
    lateinit var serverAuthenticationExchangeService: ServerAuthenticationExchangeService

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var vHabStatusRepository: VHabStatusRepository

    @MockBean
    lateinit var serverRegistrationExchangeService: ServerRegistrationExchangeService

    lateinit var server: MockRestServiceServer

    @Before
    fun setUp() {
        server = MockRestServiceServer.bindTo(restTemplate).build()
        vHabStatusRepository.clear()
    }

    @Test
    fun `authenticate light bot on wemala server`() {
        val response = ServerAuthenticationExchangeService.JwtAuthenticationResponse()
        response.token = "unit-test-auth-token"
        server.expect(MockRestRequestMatchers.requestTo("http://server.unit.test/api/auth/login"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("identifier", IsEqual.equalTo<String>("unit@test.bot")))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("password", IsEqual.equalTo<String>("unit-test-bot-password")))
                .andRespond(withSuccess(ObjectMapper().writeValueAsString(response), MediaType.APPLICATION_JSON))

        assertThat(serverAuthenticationExchangeService.authenticate()).isEqualTo("unit-test-auth-token")
        assertThat(vHabStatusRepository.getStatus()).isEqualTo(VHabStatus.STARTING)

        server.verify()
    }

    @Test
    fun `authenticate light bot on wemala server and servers first time responds unauthorized`() {
        val response = ServerAuthenticationExchangeService.JwtAuthenticationResponse()
        response.token = "unit-test-auth-token"
        `when`(serverRegistrationExchangeService.registerBot()).thenReturn(true)

        server.expect(MockRestRequestMatchers.requestTo("http://server.unit.test/api/auth/login"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("identifier", IsEqual.equalTo<String>("unit@test.bot")))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("password", IsEqual.equalTo<String>("unit-test-bot-password")))
                .andRespond(withUnauthorizedRequest())

        server.expect(MockRestRequestMatchers.requestTo("http://server.unit.test/api/auth/login"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("identifier", IsEqual.equalTo<String>("unit@test.bot")))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("password", IsEqual.equalTo<String>("unit-test-bot-password")))
                .andRespond(withSuccess(ObjectMapper().writeValueAsString(response), MediaType.APPLICATION_JSON))

        assertThat(serverAuthenticationExchangeService.authenticate()).isEqualTo("unit-test-auth-token")
        assertThat(vHabStatusRepository.getStatus()).isEqualTo(VHabStatus.STARTING)


        server.verify()
        verify(serverRegistrationExchangeService).registerBot()
    }

    @Test
    fun `authenticate light bot on wemala server and servers responds unauthorized and registration failed too`() {
        val response = ServerAuthenticationExchangeService.JwtAuthenticationResponse()
        response.token = "unit-test-auth-token"
        `when`(serverRegistrationExchangeService.registerBot()).thenReturn(false)

        server.expect(MockRestRequestMatchers.requestTo("http://server.unit.test/api/auth/login"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("identifier", IsEqual.equalTo<String>("unit@test.bot")))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("password", IsEqual.equalTo<String>("unit-test-bot-password")))
                .andRespond(withUnauthorizedRequest())

        assertThat(serverAuthenticationExchangeService.authenticate()).isNull()
        assertThat(vHabStatusRepository.getStatus()).isEqualTo(VHabStatus.AUTHENTICATION_FAILED)

        server.verify()
        verify(serverRegistrationExchangeService).registerBot()
    }

    @Test
    fun `authenticate light bot on wemala server and servers responds bad request`() {
        server.expect(MockRestRequestMatchers.requestTo("http://server.unit.test/api/auth/login"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("identifier", IsEqual.equalTo<String>("unit@test.bot")))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("password", IsEqual.equalTo<String>("unit-test-bot-password")))
                .andRespond(withBadRequest())

        assertThat(serverAuthenticationExchangeService.authenticate()).isNull()
        assertThat(vHabStatusRepository.getStatus()).isEqualTo(VHabStatus.AUTHENTICATION_FAILED)

        server.verify()
    }

    @Test
    fun `authenticate light bot on wemala server and servers responds conflict`() {
        server.expect(MockRestRequestMatchers.requestTo("http://server.unit.test/api/auth/login"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("identifier", IsEqual.equalTo<String>("unit@test.bot")))
                .andExpect(MockRestRequestMatchers.jsonPath<String>("password", IsEqual.equalTo<String>("unit-test-bot-password")))
                .andRespond(withStatus(HttpStatus.CONFLICT))

        assertThat(serverAuthenticationExchangeService.authenticate()).isNull()
        assertThat(vHabStatusRepository.getStatus()).isEqualTo(VHabStatus.AUTHENTICATION_FAILED)

        server.verify()
    }
}