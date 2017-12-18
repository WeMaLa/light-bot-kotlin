package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.Accessory
import io.iconect.lightbot.domain.AccessoryRepository
import io.iconect.lightbot.domain.service.Service
import io.iconect.lightbot.infrastructure.model.AccessoriesDto
import io.iconect.lightbot.infrastructure.model.AccessoryDto
import io.iconect.lightbot.infrastructure.model.DefaultSpringErrorDto
import io.iconect.lightbot.infrastructure.model.ServiceDto
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value = "accessories", description = "Operations pertaining to products in Online Store")
class AccessoryController @Autowired constructor(private val accessoryRepository: AccessoryRepository) {

    @ApiOperation(value = "Loads all existing rooms.")
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success")),
        (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/accessories"], method = [(RequestMethod.GET)], produces = ["application/hap+json"])
    fun findAllRooms(): ResponseEntity<AccessoriesDto> {
        val accessories = accessoryRepository.findAll().map { a -> mapToDto(a) }
        return ResponseEntity.ok<AccessoriesDto>(AccessoriesDto(accessories))
    }

    private fun mapToDto(accessory: Accessory): AccessoryDto {
        return AccessoryDto(accessory.instanceId, accessory.services.map { s -> maoToDto(s) })
    }

    private fun maoToDto(service: Service): ServiceDto {
        val type = service.uuid.split("-")[0].trimStart('0')
        return ServiceDto(service.instanceId, type)
    }

}