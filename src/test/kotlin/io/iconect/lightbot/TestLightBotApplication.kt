package io.iconect.lightbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import springfox.documentation.swagger2.annotations.EnableSwagger2


@SpringBootApplication
@EnableSwagger2
class TestLightBotApplication {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder()
                .requestFactory(HttpComponentsClientHttpRequestFactory()) // apply HTTP PATCH support
                .build()
    }

}

fun main(args: Array<String>) {
    runApplication<LightBotApplication>(*args)
}
