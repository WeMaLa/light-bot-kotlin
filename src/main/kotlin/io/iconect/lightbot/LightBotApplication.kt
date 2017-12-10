package io.iconect.lightbot

import io.iconect.lightbot.application.RoomInitializer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@SpringBootApplication
@EnableSwagger2
class LightBotApplication {

    @Bean
    fun init(roomInitializer: RoomInitializer) = CommandLineRunner {
        roomInitializer.initializeRooms()
    }

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .groupName("api")
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/api/.*"))
                .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("Lightbot REST services")
                .version("1.0")
                .build()
    }
}

fun main(args: Array<String>) {
    runApplication<LightBotApplication>(*args)
}
