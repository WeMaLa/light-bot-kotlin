package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import io.iconect.lightbot.infrastructure.model.ErrorMessageDto
import io.iconect.lightbot.infrastructure.model.RoomDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class RoomController {

    @Autowired
    lateinit private var roomRepository: RoomRepository

    @RequestMapping(value = "/api/rooms", method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    fun findAllRooms(): ResponseEntity<List<RoomDto>> {
        val rooms = roomRepository.findAll()
                .map { r -> mapToRoomDto(r) }

        return ResponseEntity.ok<List<RoomDto>>(rooms)
    }

    @RequestMapping(value = "/api/room/{identifier}", method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    fun findRoom(@PathVariable identifier: String): ResponseEntity<Any> {
        val room = roomRepository.find(identifier)
                ?: return ResponseEntity(ErrorMessageDto("Room '$identifier' not found."), HttpStatus.NOT_FOUND)

        return ResponseEntity.ok(mapToRoomDto(room))
    }

    private fun mapToRoomDto(room: Room): RoomDto {
        val roomDto = RoomDto(room.identifier, room.designation)
        roomDto.add(linkTo(methodOn(RoomController::class.java).findRoom(room.identifier)).withSelfRel())
        return roomDto
    }

}