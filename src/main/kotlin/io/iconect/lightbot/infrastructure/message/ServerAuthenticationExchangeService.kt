package io.iconect.lightbot.infrastructure.message

import io.iconect.lightbot.infrastructure.configuration.Configuration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class ServerAuthenticationExchangeService @Autowired constructor(
        private var botConfiguration: Configuration,
        private var restTemplate: RestTemplate) {

    private val log = LoggerFactory.getLogger(ServerAuthenticationExchangeService::class.java)

    fun authenticate(): String? {
        val httpEntity = HttpEntity<Any>(UserAuthenticationRequest(botConfiguration.bot!!.identifier, botConfiguration.bot!!.password))
        return try {
            val exchange = restTemplate.exchange(botConfiguration.server!!.url + "/api/auth/login", HttpMethod.POST, httpEntity, JwtAuthenticationResponse::class.java)
            return exchange.body?.token
        } catch (e: HttpClientErrorException) {
            log.error("Authenticaton bot on iconect server failed with code '${e.statusCode}' and message '${e.message}'")
            null
        }
    }

    data class UserAuthenticationRequest internal constructor(val identifier: String, val password: String)

    class JwtAuthenticationResponse {
        var token: String = ""
    }
}