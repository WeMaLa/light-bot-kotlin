package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import io.iconect.lightbot.infrastructure.model.DefaultSpringErrorDto
import io.iconect.lightbot.infrastructure.model.ErrorMessageDto
import io.iconect.lightbot.infrastructure.model.RoomDto
import io.swagger.annotations.*
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
class RoomController @Autowired constructor(private var roomRepository: RoomRepository) {

    @ApiOperation(value = "Loads all existing rooms.")
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success")),
        (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/rooms"], method = [(RequestMethod.GET)], produces = ["application/json"])
    fun findAllRooms(): ResponseEntity<List<RoomDto>> {
        val rooms = roomRepository.findAll()
                .map { r -> mapToRoomDto(r) }

        return ResponseEntity.ok<List<RoomDto>>(rooms)
    }

    @ApiOperation(value = "Loads a specific room for a given identifier.")
    @ApiImplicitParams(ApiImplicitParam(name = "identifier", value = "Unique room identifier", required = true, dataType = "string", paramType = "query", example = "kitchen-1"))
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success", response = RoomDto::class)),
        (ApiResponse(code = 404, message = "Room for identifier not found", response = ErrorMessageDto::class)),
        (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/room/{identifier}"], method = [(RequestMethod.GET)], produces = ["application/json"])
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