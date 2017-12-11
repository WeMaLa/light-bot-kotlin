package io.iconect.lightbot.infrastructure.message

import io.iconect.lightbot.infrastructure.configuration.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ServerAuthenticationExchangeService @Autowired constructor(
        private var botConfiguration: Configuration,
        private var restTemplate: RestTemplate) {

    fun authenticate(): String? {
        val httpEntity = HttpEntity<Any>(UserAuthenticationRequest(botConfiguration.bot!!.identifier, botConfiguration.bot!!.password))
        val exchange = restTemplate.exchange(botConfiguration.server!!.url + "/api/auth/login", HttpMethod.POST, httpEntity, JwtAuthenticationResponse::class.java)
        return exchange.body.token
    }

    data class UserAuthenticationRequest internal constructor(val identifier: String, val password: String)

    class JwtAuthenticationResponse {
        var token: String = ""
    }
}