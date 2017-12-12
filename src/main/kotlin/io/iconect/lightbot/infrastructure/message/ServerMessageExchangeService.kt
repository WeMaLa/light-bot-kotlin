package io.iconect.lightbot.infrastructure.message

import io.iconect.lightbot.infrastructure.configuration.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ServerMessageExchangeService @Autowired constructor(
        private var botConfiguration: Configuration,
        private var restTemplate: RestTemplate,
        private var serverAuthenticationExchangeService: ServerAuthenticationExchangeService) {

    fun retrieveMessages(): List<Message> {
        val token = serverAuthenticationExchangeService.authenticate()
        val httpHeaders = HttpHeaders()
        httpHeaders.set("Authorization", token)
        val httpEntity = HttpEntity<Any>(httpHeaders)
        val exchange = restTemplate.exchange(botConfiguration.server!!.url + "/api/messages", HttpMethod.GET, httpEntity, MessageResponse::class.java)
        return exchange.body?.content!!.filter { m -> m.status == MessageStatus.RECEIVED }
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