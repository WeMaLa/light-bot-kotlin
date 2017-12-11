package io.iconect.lightbot.infrastructure.message

import io.iconect.lightbot.infrastructure.configuration.Configuration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class ServerAuthenticationExchangeService @Autowired constructor(
        private var botConfiguration: Configuration,
        private var restTemplate: RestTemplate,
        private var serverRegistrationExchangeService: ServerRegistrationExchangeService) {

    private val log = LoggerFactory.getLogger(ServerAuthenticationExchangeService::class.java)

    fun authenticate(): String? {
        return try {
            return authenticate(botConfiguration.bot!!.identifier, botConfiguration.bot!!.password)
        } catch (e: HttpClientErrorException) {
            log.error("Authenticaton bot on iconect server failed with code '${e.statusCode}' and message '${e.message}'")

            if (e.statusCode == HttpStatus.UNAUTHORIZED) {
                log.info("Received UNAUTHORIZED while authentication. Register a new bot")
                if (serverRegistrationExchangeService.registerBot()) {
                    try {
                        return authenticate(botConfiguration.bot!!.identifier, botConfiguration.bot!!.password)
                    } catch (e: HttpClientErrorException) {
                        log.error("Authenticaton bot on iconect server failed with code '${e.statusCode}' and message '${e.message}'")
                    }
                }
            }

            null
        }
    }

    private fun authenticate(identifier: String, password: String): String {
        val httpEntity = HttpEntity<Any>(UserAuthenticationRequest(identifier, password))
        val exchange = restTemplate.exchange(botConfiguration.server!!.url + "/api/auth/login", HttpMethod.POST, httpEntity, JwtAuthenticationResponse::class.java)
        return exchange.body!!.token
    }

    data class UserAuthenticationRequest internal constructor(val identifier: String, val password: String)

    class JwtAuthenticationResponse {
        var token: String = ""
    }
}