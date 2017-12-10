package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import io.iconect.lightbot.infrastructure.model.RoomDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class RoomController {

    @Autowired
    lateinit private var roomRepository: RoomRepository

    @RequestMapping(value = "/api/rooms", method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    fun getAllRooms(): ResponseEntity<List<RoomDto>> {
        val rooms = roomRepository.loadAll()
                .map { r -> mapToRoomDto(r) }

        return ResponseEntity.ok<List<RoomDto>>(rooms)
    }

    private fun mapToRoomDto(room: Room): RoomDto {
        val roomDto = RoomDto(room.identifier, room.designation)
        roomDto.add(linkTo(methodOn(RoomController::class.java).getAllRooms()).withSelfRel())
        return roomDto
    }

}