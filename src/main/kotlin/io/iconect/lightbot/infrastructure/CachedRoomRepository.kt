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

    override fun loadAll(): List<Room> {
        return rooms
    }

    override fun clear() {
        rooms.clear()
    }
}