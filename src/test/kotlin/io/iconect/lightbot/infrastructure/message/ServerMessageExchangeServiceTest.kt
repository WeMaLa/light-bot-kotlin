package io.iconect.lightbot.infrastructure.message

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.match.MockRestRequestMatchers.header
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.web.client.RestTemplate


@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
class ServerMessageExchangeServiceTest {

    @Autowired
    lateinit var serverMessageExchangeService: ServerMessageExchangeService

    @MockBean
    lateinit var serverAuthenticationExchangeService: ServerAuthenticationExchangeService

    @Autowired
    lateinit var restTemplate: RestTemplate

    lateinit var server: MockRestServiceServer

    @Before
    fun setUp() {
        server = MockRestServiceServer.bindTo(restTemplate).build()
    }

    @Test
    fun `retrieve messages`() {
        `when`(serverAuthenticationExchangeService.authenticate()).thenReturn("unit-test-auth-token")

        val httpHeaders = HttpHeaders()
        httpHeaders.set("content-type", "application/json;charset=UTF-8 ")
        httpHeaders.set("date", "Tue, 12 Dec 2017 19:59:50 GMT")
        httpHeaders.set("date-iso8601", "2017-12-12T19:59:50.099-00:00")
        val response = withStatus(HttpStatus.OK).body("")
                .body(createResponse())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(httpHeaders)
        server.expect(MockRestRequestMatchers.requestTo("http://server.unit.test/api/messages"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andExpect(header("Authorization", "unit-test-auth-token"))
                .andRespond(response)

        assertThat(serverMessageExchangeService.retrieveMessages())
                .extracting("identifier", "content", "status")
                .containsExactly(tuple("AWA6_vR3A1S3ubG7cRd1", "message2", ServerMessageExchangeService.MessageStatus.RECEIVED))

        server.verify()
    }

    private fun createResponse(): String {
        return "{\n" +
                "  \"content\": [\n" +
                "    {\n" +
                "      \"identifier\": \"AWA7AQKAA1S3ubG7cRd3\",\n" +
                "      \"content\": \"message3\",\n" +
                "      \"createDate\": \"2017-12-09 11:20:10\",\n" +
                "      \"status\": \"READ\",\n" +
                "      \"_links\": {\n" +
                "        \"self\": {\n" +
                "          \"href\": \"/api/message/AWA7AQKAA1S3ubG7cRd3\"\n" +
                "        },\n" +
                "        \"channel\": {\n" +
                "          \"href\": \"/api/channel/AWA6_ozSA1S3ubG7cRdx\"\n" +
                "        },\n" +
                "        \"sender\": {\n" +
                "          \"href\": \"/api/contact/mail@larmic.de\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"identifier\": \"AWA6_vR3A1S3ubG7cRd1\",\n" +
                "      \"content\": \"message2\",\n" +
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
                "      \"content\": \"message1\",\n" +
                "      \"createDate\": \"2017-12-09 11:17:29\",\n" +
                "      \"status\": \"SEND\",\n" +
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
}