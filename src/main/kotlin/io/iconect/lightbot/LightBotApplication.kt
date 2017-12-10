package io.iconect.lightbot

import io.iconect.lightbot.application.RoomInitializer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class LightBotApplication {

    @Bean
    fun init(roomInitializer: RoomInitializer) = CommandLineRunner {
        roomInitializer.initializeRooms()
    }
}

fun main(args: Array<String>) {
    runApplication<LightBotApplication>(*args)
}
