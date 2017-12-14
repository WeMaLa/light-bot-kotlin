package io.iconect.lightbot.infrastructure.message

import io.iconect.lightbot.infrastructure.configuration.Configuration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class ServerMessageExchangeService @Autowired constructor(
        private var botConfiguration: Configuration,
        private var restTemplate: RestTemplate,
        private var serverAuthenticationExchangeService: ServerAuthenticationExchangeService) {

    private val log = LoggerFactory.getLogger(ServerMessageExchangeService::class.java)

    fun retrieveMessages(): List<Message> {
        val token = serverAuthenticationExchangeService.authenticate()

        return if (token != null) {
            val httpEntity = createHttpEntity(token)
            val messages = loadUnreadMessages(httpEntity)

            messages.forEach { m -> markAsRead(m.identifier, httpEntity) }

            return messages
        } else {
            emptyList()
        }
    }

    private fun markAsRead(messageIdentifier: String, httpEntity: HttpEntity<Any>) {
        try {
            val url = botConfiguration.server!!.url + "/api/messages/$messageIdentifier/read"
            restTemplate.exchange(url, HttpMethod.PATCH, httpEntity, String::class.java)
        } catch (e: HttpClientErrorException) {
            log.error("Mark message '$messageIdentifier' as read on iconect server failed with code '${e.statusCode}' and message '${e.message}'")
        }
    }

    private fun loadUnreadMessages(httpEntity: HttpEntity<Any>): List<Message> {
        return try {
            val url = botConfiguration.server!!.url + "/api/messages?status=SEND&status=RECEIVED"
            restTemplate.exchange(url, HttpMethod.GET, httpEntity, MessageResponse::class.java).body?.content!!.asList()
        } catch (e: HttpClientErrorException) {
            log.error("Retrieve message from iconect server failed with code '${e.statusCode}' and message '${e.message}'")
            emptyList()
        }
    }

    private fun createHttpEntity(token: String?): HttpEntity<Any> {
        val httpHeaders = HttpHeaders()
        httpHeaders.set("Authorization", token)
        return HttpEntity(httpHeaders)
    }

    class MessageResponse {
        var content: Array<Message> = arrayOf()
    }

    class Message {
        var identifier: String = ""
        var content: String = ""
        var status: MessageStatus = MessageStatus.READ
    }

    enum class MessageStatus {
        SEND, RECEIVED, READ
    }
}