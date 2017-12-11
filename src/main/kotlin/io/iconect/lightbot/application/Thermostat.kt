package io.iconect.lightbot.application

import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Thermostat @Autowired constructor(private var roomRepository: RoomRepository) {

    fun adjust(heaterIdentifier: String, degree: Short): Boolean {
        val room = findRoomWithHeater(heaterIdentifier)

        if (room != null) {
            findHeaterInRoom(room, heaterIdentifier).heatTo(degree)
            roomRepository.store(room)
            return true
        }
        return false
    }

    private fun findHeaterInRoom(room: Room?, heaterIdentifier: String) =
            room!!.heaters.find { h -> h.identifier == heaterIdentifier }!!

    private fun findRoomWithHeater(heaterIdentifier: String) = roomRepository.findAll()
            .find { r -> r.heaters.find { h -> h.identifier == heaterIdentifier } != null }

}