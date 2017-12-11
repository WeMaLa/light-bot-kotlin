package io.iconect.lightbot.application

import io.iconect.lightbot.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoomInitializer @Autowired constructor(private var roomRepository: RoomRepository) {

    fun initializeRooms() {
        val kitchen = Room.Builder("kitchen-1")
                .designation("kitchen")
                .windows(listOf(
                        Window.Builder("kitchen-1-window-1").designation("window left").build(),
                        Window.Builder("kitchen-1-window-2").designation("window right").build())
                )
                .heaters(listOf(
                        Heater.Builder("kitchen-1-heater-1").designation("floor heating").build())
                )
                .lamps(listOf(
                        Lamp.Builder("kitchen-1-lamp-1").designation("overhead lights").build())
                )
                .build()

        roomRepository.store(kitchen)
    }

}