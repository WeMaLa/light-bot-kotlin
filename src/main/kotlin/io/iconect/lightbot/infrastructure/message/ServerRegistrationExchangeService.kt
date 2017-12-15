package io.iconect.lightbot.infrastructure.message

import io.iconect.lightbot.infrastructure.configuration.Configuration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

@Service
class ServerRegistrationExchangeService @Autowired constructor(
        private var botConfiguration: Configuration,
        private var restTemplate: RestTemplate) {

    private val log = LoggerFactory.getLogger(ServerRegistrationExchangeService::class.java)

    fun registerBot(): Boolean {
        log.info("Register new bot on iconect server")
        val httpEntity = HttpEntity<Any>(UserRegistrationRequest(botConfiguration.bot!!.identifier, botConfiguration.bot!!.password, botConfiguration.bot!!.username))

        return try {
            restTemplate.exchange(botConfiguration.server!!.url + "/api/user", HttpMethod.POST, httpEntity, Any::class.java)
            true
        } catch (e: Exception) {
            if (e is HttpStatusCodeException) {
                log.error("Register bot on iconect server failed with code '${e.statusCode}' and message '${e.message}'")
            } else {
                log.error("Register bot on iconect server failed with message '${e.message}'")
            }
            false
        }
    }

    data class UserRegistrationRequest internal constructor(val email: String, val password: String, val username: String)

}