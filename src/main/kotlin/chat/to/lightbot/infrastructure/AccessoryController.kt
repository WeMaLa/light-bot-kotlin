package chat.to.lightbot.infrastructure

import chat.to.lightbot.domain.hap.AccessoryRepository
import chat.to.lightbot.infrastructure.model.*
import io.iconect.lightbot.infrastructure.model.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value = "accessories", description = "vHAB accessories")
class AccessoryController @Autowired constructor(private val accessoryRepository: AccessoryRepository) {

    @ApiOperation(value = "Loads all existing accessories.")
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success")),
        (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/accessories"], method = [(RequestMethod.GET)], produces = ["application/hap+json"])
    fun findAllAccessories(): ResponseEntity<AccessoriesDto> {
        val accessories = accessoryRepository.findAll().map { a -> AccessoryDto.from(a) }
        return ResponseEntity.ok<AccessoriesDto>(AccessoriesDto(accessories))
    }

    @ApiOperation(value = "Loads accessory for a specific aid.")
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success")),
        (ApiResponse(code = 405, message = "Accessory not found", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/accessories/{aid}"], method = [(RequestMethod.GET)], produces = ["application/hap+json"])
    fun findAccessory(@PathVariable aid: Int): ResponseEntity<Any> {
        val accessory = accessoryRepository.findByInstanceId(aid)

        if (accessory != null) {
            return ResponseEntity.ok(AccessoryDto.from(accessory))
        }

        return ResponseEntity(ErrorMessageDto("Accessory $aid not found."), HttpStatus.NOT_FOUND)
    }

    @ApiOperation(value = "Loads all services of an accessory for a specific aid.")
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success")),
        (ApiResponse(code = 405, message = "Accessory not found", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/accessories/{aid}/services"], method = [(RequestMethod.GET)], produces = ["application/hap+json"])
    fun findAccessoryServices(@PathVariable aid: Int): ResponseEntity<Any> {
        val accessory = accessoryRepository.findByInstanceId(aid)

        if (accessory != null) {
            return ResponseEntity.ok(accessory.services.map { ServiceDto.from(it) })
        }

        return ResponseEntity(ErrorMessageDto("Accessory $aid not found."), HttpStatus.NOT_FOUND)
    }

    @ApiOperation(value = "Loads all services of an accessory for a specific aid and iid")
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success")),
        (ApiResponse(code = 405, message = "Accessory not found", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/accessories/{aid}/services/{iid}"], method = [(RequestMethod.GET)], produces = ["application/hap+json"])
    fun findAccessoryService(@PathVariable aid: Int, @PathVariable iid: Int): ResponseEntity<Any> {
        val accessory = accessoryRepository.findByInstanceId(aid)

        if (accessory != null) {
            val service = accessory.services.find { it.instanceId == iid }

            if (service != null) {
                return ResponseEntity.ok(ServiceDto.from(service))
            }

            return ResponseEntity(ErrorMessageDto("Service $iid of accessory $aid not found."), HttpStatus.NOT_FOUND)
        }

        return ResponseEntity(ErrorMessageDto("Accessory $aid not found."), HttpStatus.NOT_FOUND)
    }

    @ApiOperation(value = "Loads all characteristics of a services of an accessory for a specific aid and iid")
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success")),
        (ApiResponse(code = 405, message = "Accessory not found", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/accessories/{aid}/services/{iid}/characteristics"], method = [(RequestMethod.GET)], produces = ["application/hap+json"])
    fun findAccessoryServiceCharacteristics(@PathVariable aid: Int, @PathVariable iid: Int): ResponseEntity<Any> {
        val accessory = accessoryRepository.findByInstanceId(aid)

        if (accessory != null) {
            val service = accessory.services.find { it.instanceId == iid }

            if (service != null) {
                return ResponseEntity.ok(service.characteristics.map { CharacteristicDto.from(it) })
            }

            return ResponseEntity(ErrorMessageDto("Service $iid of accessory $aid not found."), HttpStatus.NOT_FOUND)
        }

        return ResponseEntity(ErrorMessageDto("Accessory $aid not found."), HttpStatus.NOT_FOUND)
    }

    @ApiOperation(value = "Loads a characteristic of a services of an accessory for a specific aid, siid and iid")
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success")),
        (ApiResponse(code = 405, message = "Accessory not found", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/accessories/{aid}/services/{siid}/characteristics/{iid}"], method = [(RequestMethod.GET)], produces = ["application/hap+json"])
    fun findAccessoryServiceCharacteristic(@PathVariable aid: Int, @PathVariable siid: Int, @PathVariable iid: Int): ResponseEntity<Any> {
        val accessory = accessoryRepository.findByInstanceId(aid)

        if (accessory != null) {
            val service = accessory.services.find { it.instanceId == siid }

            if (service != null) {
                val characteristic = service.characteristics.find { it.instanceId == iid }

                if (characteristic != null) {
                    return ResponseEntity.ok(CharacteristicDto.from(characteristic))
                }

                return ResponseEntity(ErrorMessageDto("Characteristic $iid of service $siid of accessory $aid not found."), HttpStatus.NOT_FOUND)
            }

            return ResponseEntity(ErrorMessageDto("Service $siid of accessory $aid not found."), HttpStatus.NOT_FOUND)
        }

        return ResponseEntity(ErrorMessageDto("Accessory $aid not found."), HttpStatus.NOT_FOUND)
    }

}