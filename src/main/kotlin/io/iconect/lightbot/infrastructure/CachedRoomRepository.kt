package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import org.springframework.stereotype.Service

@Service
class CachedRoomRepository : RoomRepository {

    override fun store(room: Room) {
        rooms.add(room)
    }

    override fun loadAll(): List<Room> {
        return CachedRoomRepository.rooms
    }

    override fun clear() {
        rooms.clear()
    }

    companion object {
        private var rooms = mutableListOf<Room>()
    }
}