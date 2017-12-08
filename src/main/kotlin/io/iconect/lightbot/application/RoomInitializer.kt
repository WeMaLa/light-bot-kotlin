package io.iconect.lightbot.application

import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoomInitializer {

    @Autowired
    lateinit var roomRepository: RoomRepository

    fun initializeRooms() {
        val kitchen = Room.Builder("kitchen-1")
                .designation("kitchen")
                .build()

        roomRepository.store(kitchen)
    }

}