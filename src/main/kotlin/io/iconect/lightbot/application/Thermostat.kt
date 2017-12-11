package io.iconect.lightbot.application

import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Thermostat @Autowired constructor(private var roomRepository: RoomRepository) {

    fun adjust(heaterIdentifier: String, degree: Short) {
        if (degree < 0) {
            throw IllegalArgumentException("Heater does not support frost")
        }

        val room = findRoomWithHeater(heaterIdentifier)
                ?: throw IllegalArgumentException("Room with heater '$heaterIdentifier' not found")

        val heater = findHeaterInRoom(room, heaterIdentifier)

        if (degree > heater.maxDegree) {
            throw IllegalArgumentException("Heater is limited to 60 degree")
        }

        heater.heatTo(if (degree >= 0) degree else 0)
        roomRepository.store(room)
    }

    private fun findHeaterInRoom(room: Room?, heaterIdentifier: String) =
            room!!.heaters.find { h -> h.identifier == heaterIdentifier }!!

    private fun findRoomWithHeater(heaterIdentifier: String) = roomRepository.findAll()
            .find { r -> r.heaters.find { h -> h.identifier == heaterIdentifier } != null }

}