package chat.to.lightbot.infrastructure.message

import chat.to.lightbot.domain.hap.VHabStatus
import chat.to.lightbot.domain.hap.VHabStatusRepository
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.match.MockRestRequestMatchers.*
import org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.web.client.RestTemplate

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles("unittest")
class ServerMessageExchangeServiceTest {

    @Autowired
    lateinit var serverMessageExchangeService: ServerMessageExchangeService

    @MockBean
    lateinit var serverAuthenticationExchangeService: ServerAuthenticationExchangeService

    @Autowired
    lateinit var vHabStatusRepository: VHabStatusRepository

    @Autowired
    lateinit var restTemplate: RestTemplate

    lateinit var server: MockRestServiceServer

    @BeforeEach
    fun setUp() {
        server = MockRestServiceServer.bindTo(restTemplate).build()
        vHabStatusRepository.clear()
    }

    @Test
    fun `retrieve messages`() {
        whenever(serverAuthenticationExchangeService.authenticate()).thenReturn("unit-test-auth-token")

        val httpHeaders = HttpHeaders()
        httpHeaders.set("content-type", "application/json;charset=UTF-8 ")
        httpHeaders.set("date", "Tue, 12 Dec 2017 19:59:50 GMT")
        httpHeaders.set("date-iso8601", "2017-12-12T19:59:50.099-00:00")
        val response = withStatus(HttpStatus.OK).body("")
                .body(createResponse())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(httpHeaders)
        server.expect(requestTo("http://server.unit.test/api/messages?status=SEND&status=RECEIVED"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andExpect(header("Authorization", "unit-test-auth-token"))
                .andRespond(response)
        server.expect(requestTo("http://server.unit.test/api/message/AWA6_vR3A1S3ubG7cRd1/read"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.PATCH))
                .andExpect(header("Authorization", "unit-test-auth-token"))
                .andRespond(response)
        server.expect(requestTo("http://server.unit.test/api/message/AWA6_o33A1S3ubG7cRdz/read"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.PATCH))
                .andExpect(header("Authorization", "unit-test-auth-token"))
                .andRespond(response)

        assertThat(serverMessageExchangeService.retrieveMessages())
                .extracting("raw", "channel")
                .containsExactly(
                       tuple( "message2", "AWA6_ozSA1S3ubG7cRdx"),
                       tuple( "message1", "AWA6_ozSA1S3ubG7cRdx"))
        assertThat(vHabStatusRepository.getStatus()).isEqualTo(VHabStatus.OK)

        server.verify()
    }

    @Test
    fun `retrieve messages and servers responds bad request`() {
        whenever(serverAuthenticationExchangeService.authenticate()).thenReturn("unit-test-auth-token")

        server.expect(requestTo("http://server.unit.test/api/messages?status=SEND&status=RECEIVED"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andExpect(header("Authorization", "unit-test-auth-token"))
                .andRespond(withBadRequest())

        assertThat(serverMessageExchangeService.retrieveMessages()).isEmpty()
        assertThat(vHabStatusRepository.getStatus()).isEqualTo(VHabStatus.RECEIVE_MESSAGES_FAILED)

        server.verify()
    }

    @Test
    fun `retrieve messages and mark messages responds bad request`() {
        whenever(serverAuthenticationExchangeService.authenticate()).thenReturn("unit-test-auth-token")

        val response = withStatus(HttpStatus.OK).body("")
                .body(createResponse())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        server.expect(requestTo("http://server.unit.test/api/messages?status=SEND&status=RECEIVED"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andExpect(header("Authorization", "unit-test-auth-token"))
                .andRespond(response)
        server.expect(requestTo("http://server.unit.test/api/message/AWA6_vR3A1S3ubG7cRd1/read"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.PATCH))
                .andExpect(header("Authorization", "unit-test-auth-token"))
                .andRespond(response)
        server.expect(requestTo("http://server.unit.test/api/message/AWA6_o33A1S3ubG7cRdz/read"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.PATCH))
                .andExpect(header("Authorization", "unit-test-auth-token"))
                .andRespond(withBadRequest())

        assertThat(serverMessageExchangeService.retrieveMessages())
                .extracting("raw", "channel")
                .containsExactly(
                        tuple( "message2", "AWA6_ozSA1S3ubG7cRdx"),
                        tuple( "message1", "AWA6_ozSA1S3ubG7cRdx"))
        assertThat(vHabStatusRepository.getStatus()).isEqualTo(VHabStatus.MARK_MESSAGES_FAILED)

        server.verify()
    }

    @Test
    fun `retrieve messages when authentication fails`() {
        whenever(serverAuthenticationExchangeService.authenticate()).thenReturn(null)

        assertThat(serverMessageExchangeService.retrieveMessages()).isEmpty()
    }

    @Test
    fun `retrieve messages when messages are empty`() {
        whenever(serverAuthenticationExchangeService.authenticate()).thenReturn("unit-test-auth-token")

        val httpHeaders = HttpHeaders()
        httpHeaders.set("content-type", "application/json;charset=UTF-8 ")
        httpHeaders.set("date", "Tue, 12 Dec 2017 19:59:50 GMT")
        httpHeaders.set("date-iso8601", "2017-12-12T19:59:50.099-00:00")
        val response = withStatus(HttpStatus.OK).body("")
                .body(createEmptyMessagesResponse())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(httpHeaders)
        server.expect(requestTo("http://server.unit.test/api/messages?status=SEND&status=RECEIVED"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andExpect(header("Authorization", "unit-test-auth-token"))
                .andRespond(response)

        assertThat(serverMessageExchangeService.retrieveMessages()).isEmpty()
        assertThat(vHabStatusRepository.getStatus()).isEqualTo(VHabStatus.OK)
    }

    @Test
    fun `send message`() {
        whenever(serverAuthenticationExchangeService.authenticate()).thenReturn("unit-test-auth-token")

        val messageContent = "unit-test-message-text"
        val channelIdentifier = "unit-test-channel-identifier"

        server.expect(requestTo("http://server.unit.test/api/message"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(header("Authorization", "unit-test-auth-token"))
                .andExpect(jsonPath("text", `is`(messageContent)))
                .andExpect(jsonPath("channelIdentifier", `is`(channelIdentifier)))
                .andRespond(withStatus(HttpStatus.OK))

        serverMessageExchangeService.sendMessage(channelIdentifier, messageContent)

        server.verify()
    }

    @Test
    fun `send message when authentication fails`() {
        whenever(serverAuthenticationExchangeService.authenticate()).thenReturn(null)

        serverMessageExchangeService.sendMessage("unit-test-channel-identifier", "unit-test-message-text")

        server.verify() // no server call
    }

    @Test
    fun `send message with channel identifier is empty`() {
        whenever(serverAuthenticationExchangeService.authenticate()).thenReturn("unit-test-auth-token")

        serverMessageExchangeService.sendMessage("", "unit-test-message-text")

        server.verify() // no server call
    }

    @Test
    fun `send message with message content is empty`() {
        whenever(serverAuthenticationExchangeService.authenticate()).thenReturn("unit-test-auth-token")

        serverMessageExchangeService.sendMessage("unit-test-channel-identifier", "")

        server.verify() // no server call
    }

    private fun createResponse(): String {
        return "{\n" +
                "  \"content\": [\n" +
                "    {\n" +
                "      \"identifier\": \"AWA6_vR3A1S3ubG7cRd1\",\n" +
                "      \"text\": \"message2\",\n" +
                "      \"createDate\": \"2017-12-09 11:17:55\",\n" +
                "      \"status\": \"RECEIVED\",\n" +
                "      \"_links\": {\n" +
                "        \"self\": {\n" +
                "          \"href\": \"/api/message/AWA6_vR3A1S3ubG7cRd1\"\n" +
                "        },\n" +
                "        \"channel\": {\n" +
                "          \"href\": \"/api/channel/AWA6_ozSA1S3ubG7cRdx\"\n" +
                "        },\n" +
                "        \"sender\": {\n" +
                "          \"href\": \"/api/contact/admin@iconect.io\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"identifier\": \"AWA6_o33A1S3ubG7cRdz\",\n" +
                "      \"text\": \"message1\",\n" +
                "      \"createDate\": \"2017-12-09 11:17:29\",\n" +
                "      \"status\": \"RECEIVED\",\n" +
                "      \"_links\": {\n" +
                "        \"self\": {\n" +
                "          \"href\": \"/api/message/AWA6_o33A1S3ubG7cRdz\"\n" +
                "        },\n" +
                "        \"channel\": {\n" +
                "          \"href\": \"/api/channel/AWA6_ozSA1S3ubG7cRdx\"\n" +
                "        },\n" +
                "        \"sender\": {\n" +
                "          \"href\": \"/api/contact/admin@iconect.io\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"last\": true,\n" +
                "  \"totalElements\": 3,\n" +
                "  \"totalPages\": 1,\n" +
                "  \"first\": true,\n" +
                "  \"sort\": null,\n" +
                "  \"numberOfElements\": 3,\n" +
                "  \"size\": 0,\n" +
                "  \"number\": 0\n" +
                "}"
    }

    private fun createEmptyMessagesResponse(): String {
        return "{\n" +
                "  \"content\": [],\n" +
                "  \"last\": true,\n" +
                "  \"totalElements\": 3,\n" +
                "  \"totalPages\": 1,\n" +
                "  \"first\": true,\n" +
                "  \"sort\": null,\n" +
                "  \"numberOfElements\": 3,\n" +
                "  \"size\": 0,\n" +
                "  \"number\": 0\n" +
                "}"
    }
}