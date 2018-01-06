package io.iconect.lightbot.application.message.command

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.iconect.lightbot.application.message.ServerMessagesScheduler
import io.iconect.lightbot.domain.hap.AccessoryRepository
import io.iconect.lightbot.domain.hap.service.characteristic.Permission
import io.iconect.lightbot.domain.hap.service.characteristic.WritableCharacteristic
import io.iconect.lightbot.domain.message.content.HapWritingCharacteristicsMessageContent
import io.iconect.lightbot.infrastructure.message.model.HapStatusCode
import org.slf4j.LoggerFactory

class HapWritingCharacteristicsMessageCommand(private val accessoryRepository: AccessoryRepository) : MessageCommand<HapWritingCharacteristicsMessageContent> {

    private val log = LoggerFactory.getLogger(ServerMessagesScheduler::class.java)

    override fun executeMessage(content: HapWritingCharacteristicsMessageContent): String {
        // find accessories not existing
        val hapWritingCharacteristicErrors = content.content.characteristics
                .filter { accessoryRepository.findByInstanceId(it.aid) == null }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70409.code) }
                .toMutableList()

        // find characteristics not existing
        hapWritingCharacteristicErrors.addAll(content.content.characteristics
                .filter { c -> hapWritingCharacteristicErrors.find { it.aid == c.aid } == null }
                .filter { accessoryRepository.findByInstanceId(it.aid)!!.findCharacteristic(it.iid) == null }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70409.code) })

        // find characteristics not writable
        hapWritingCharacteristicErrors.addAll(content.content.characteristics
                .filter { c -> hapWritingCharacteristicErrors.find { it.aid == c.aid } == null }
                .filter { !accessoryRepository.findByInstanceId(it.aid)!!.findCharacteristic(it.iid)!!.permissions.contains(Permission.PAIRED_WRITE) }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70404.code) })

        val hapWritingSuccesses = mutableListOf<HapWritingCharacteristicSuccess>()

        // write existing and writable characteristics
        content.content.characteristics
                .filter { c -> hapWritingCharacteristicErrors.find { it.aid == c.aid } == null }
                .forEach {
                    val characteristic = accessoryRepository.findByInstanceId(it.aid)!!.findCharacteristic(it.iid)
                    if (characteristic is WritableCharacteristic) {
                        try {
                            log.info("Switching characteristic '${it.iid}' value from '${characteristic.value}' to '${it.value}'")
                            characteristic.adjustValue(it.value)
                            hapWritingSuccesses.add(HapWritingCharacteristicSuccess(it.aid, it.iid, it.value))
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