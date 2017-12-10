package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import org.springframework.stereotype.Service

@Service
class CachedRoomRepository : RoomRepository {

    private val rooms = mutableListOf<Room>()

    override fun store(room: Room) {
        rooms.add(room)
    }

    override fun findAll(): List<Room> {
        return rooms
    }

    override fun find(identifier: String): Room? {
        return rooms.find { r -> r.identifier == identifier }
    }

    override fun clear() {
        rooms.clear()
    }
}