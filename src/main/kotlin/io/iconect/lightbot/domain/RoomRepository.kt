package io.iconect.lightbot.domain

interface RoomRepository {

    fun store(room: Room)
    fun loadAll(): List<Room>

    // TODO only used in unit test. Find a better way
    fun clear()

}