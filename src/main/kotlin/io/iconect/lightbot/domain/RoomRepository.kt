package io.iconect.lightbot.domain

interface RoomRepository {

    fun store(room: Room)
    fun findAll(): List<Room>
    fun find(identifier: String): Room?

    // TODO only used in unit test. Find a better way
    fun clear()

}