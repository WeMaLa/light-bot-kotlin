package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.application.Thermostat
import io.iconect.lightbot.infrastructure.model.DefaultSpringErrorDto
import io.iconect.lightbot.infrastructure.model.ErrorMessageDto
import io.iconect.lightbot.infrastructure.model.RoomDto
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class HeaterController @Autowired constructor(private var thermostat: Thermostat) {

    @ApiOperation(value = "Loads a specific room for a given identifier.")
    @ApiImplicitParams(ApiImplicitParam(name = "identifier", value = "Unique heater identifier", required = true, dataType = "string", paramType = "query", example = "heater-1"))
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success", response = RoomDto::class)),
        (ApiResponse(code = 404, message = "Room for identifier not found", response = ErrorMessageDto::class)), (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)), (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/heater/{identifier}"], method = [(RequestMethod.PUT)], produces = ["application/json"])
    fun adjust(@PathVariable identifier: String, @RequestBody degree: Short): ResponseEntity<Any> {
        thermostat.adjust(identifier, degree)
        return ResponseEntity.ok().build()
    }

}