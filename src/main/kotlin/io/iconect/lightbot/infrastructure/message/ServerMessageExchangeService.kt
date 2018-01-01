package io.iconect.lightbot.infrastructure.message

import io.iconect.lightbot.domain.VHabStatus
import io.iconect.lightbot.infrastructure.configuration.Configuration
import io.iconect.lightbot.infrastructure.message.model.ServerMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

@Service
class ServerMessageExchangeService @Autowired constructor(
        private var botConfiguration: Configuration,
        private var restTemplate: RestTemplate,
        private var applicationEventPublisher: ApplicationEventPublisher,
        private var serverAuthenticationExchangeService: ServerAuthenticationExchangeService) {

    private val log = LoggerFactory.getLogger(ServerMessageExchangeService::class.java)

    fun retrieveMessages(): List<ServerMessage> {
        val token = serverAuthenticationExchangeService.authenticate()

        return if (token != null) {
            val httpEntity = createHttpEntity(token)
            val messages = loadUnreadMessages(httpEntity)

            messages.forEach { m -> markAsRead(m.identifier, httpEntity) }

            return messages.map { m -> ServerMessage(m.content, m._links.channel.href.replace("/api/channel/", "")) }
        } else {
            emptyList()
        }
    }

    private fun markAsRead(messageIdentifier: String, httpEntity: HttpEntity<Any>) {
        try {
            val url = botConfiguration.server!!.url + "/api/message/$messageIdentifier/read"
            restTemplate.exchange(url, HttpMethod.PATCH, httpEntity, String::class.java)
            applicationEventPublisher.publishEvent(VHabStatus.OK)
        } catch (e: Exception) {
            if (e is HttpStatusCodeException) {
                log.error("Mark message '$messageIdentifier' as read on iconect server failed with code '${e.statusCode}' and message '${e.message}'")
            } else {
                log.error("Mark message '$messageIdentifier' as read on iconect server failed with message '${e.message}'")
            }
            applicationEventPublisher.publishEvent(VHabStatus.MARK_MESSAGES_FAILED)
        }
    }

    private fun loadUnreadMessages(httpEntity: HttpEntity<Any>): List<Message> {
        return try {
            val url = botConfiguration.server!!.url + "/api/messages?status=SEND&status=RECEIVED"
            val messages = restTemplate.exchange(url, HttpMethod.GET, httpEntity, MessageResponse::class.java).body?.content!!.asList()
            applicationEventPublisher.publishEvent(VHabStatus.OK)
            return messages
        } catch (e: Exception) {
            if (e is HttpStatusCodeException) {
                log.error("Retrieve message from iconect server failed with code '${e.statusCode}' and message '${e.message}'")
            } else {
                log.error("Retrieve message from iconect server failed with message '${e.message}'")
            }
            applicationEventPublisher.publishEvent(VHabStatus.RECEIVE_MESSAGES_FAILED)
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
        var _links: Links = Links()

        class Links {
            var channel : Channel = Channel()

            class Channel {
                var href : String = ""
            }
        }
    }
}