package chat.to.lightbot.application.message

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import chat.to.lightbot.domain.hap.AccessoryRepository
import chat.to.lightbot.domain.hap.service.characteristic.Permission
import chat.to.lightbot.domain.hap.service.characteristic.WritableCharacteristic
import chat.to.lightbot.domain.message.ServerMessage
import chat.to.lightbot.domain.message.ServerMessageRepository
import chat.to.lightbot.domain.message.ServerMessageType
import chat.to.lightbot.infrastructure.message.model.HapStatusCode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ServerMessageHandler @Autowired constructor(
        private var serverMessageRepository: ServerMessageRepository,
        private val accessoryRepository: AccessoryRepository){

    private val log = LoggerFactory.getLogger(ServerMessageHandler::class.java)

    fun handleMessage(serverMessage: ServerMessage) {
        log.info("Start handling message '${serverMessage.raw}'")
        val messageAnswer = executeMessage(serverMessage)
        log.info("Execute message '${serverMessage.raw}' answers '$messageAnswer'")
        serverMessageRepository.sendMessage(serverMessage.channel, messageAnswer)
        log.info("Message '${serverMessage.raw}' handled")
    }

    private fun executeMessage(serverMessage: ServerMessage): String {
        if (serverMessage.type == ServerMessageType.HAP_WRITING_CHARACTERISTICS) {
            return executeHapWritingCharacteristicsMessage(serverMessage)
        }

        return "I do not understand the command '${serverMessage.raw}'"
    }

    private fun executeHapWritingCharacteristicsMessage(serverMessage: ServerMessage): String {
        val characteristics = (serverMessage.content as ServerMessage.HapWritingCharacteristics).characteristics

        // find accessories not existing
        val hapWritingCharacteristicErrors = characteristics
                .filter { accessoryRepository.findByInstanceId(it.aid) == null }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70409.code) }
                .toMutableList()

        // find characteristics not existing
        hapWritingCharacteristicErrors.addAll(characteristics
                .filter { c -> hapWritingCharacteristicErrors.find { it.aid == c.aid } == null }
                .filter { accessoryRepository.findByInstanceId(it.aid)!!.findCharacteristic(it.iid) == null }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70409.code) })

        // find characteristics not writable
        hapWritingCharacteristicErrors.addAll(characteristics
                .filter { c -> hapWritingCharacteristicErrors.find { it.aid == c.aid } == null }
                .filter { !accessoryRepository.findByInstanceId(it.aid)!!.findCharacteristic(it.iid)!!.permissions.contains(Permission.PAIRED_WRITE) }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70404.code) })

        val hapWritingSuccesses = mutableListOf<HapWritingCharacteristicSuccess>()

        // write existing and writable characteristics
        characteristics
                .filter { c -> hapWritingCharacteristicErrors.find { it.aid == c.aid } == null }
                .forEach {
                    val accessory = accessoryRepository.findByInstanceId(it.aid)!!
                    val characteristic = accessory.findCharacteristic(it.iid)
                    if (characteristic is WritableCharacteristic) {
                        try {
                            log.info("Switching characteristic '${it.iid}' value from '${characteristic.value}' to '${it.value}'")
                            characteristic.adjustValue(it.value)
                            hapWritingSuccesses.add(HapWritingCharacteristicSuccess(it.aid, it.iid, it.value))
                            accessoryRepository.store(accessory)
                        } catch (e: IllegalArgumentException) {
                            // TODO test me and check status code (i.e. minimum and maximum value)
                            hapWritingCharacteristicErrors.add(HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70404.code))
                        }
                    } else {
                        // TODO test me and check status code
                        // TODO should not happen -> server error?
                        hapWritingCharacteristicErrors.add(HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70404.code))
                    }
                }

        return if (hapWritingCharacteristicErrors.isNotEmpty()) {
            val mapper = jacksonObjectMapper()
            val errors = hapWritingSuccesses.map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C0.code) }.toMutableList()
            errors.addAll(hapWritingCharacteristicErrors)
            mapper.writeValueAsString(HapWritingCharacteristicErrors(errors))
        } else {
            val mapper = jacksonObjectMapper()
            mapper.writeValueAsString(HapWritingCharacteristicSuccesses(hapWritingSuccesses))
        }
    }

    data class HapWritingCharacteristicErrors(val characteristics: List<HapWritingCharacteristicError>)
    data class HapWritingCharacteristicError(val aid: Int, val iid: Int, val status: Int)

    data class HapWritingCharacteristicSuccesses(val characteristics: List<HapWritingCharacteristicSuccess>)
    data class HapWritingCharacteristicSuccess(val aid: Int, val iid: Int, val value: String)
}