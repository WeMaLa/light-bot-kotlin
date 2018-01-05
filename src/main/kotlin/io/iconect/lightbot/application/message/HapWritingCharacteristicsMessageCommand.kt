package io.iconect.lightbot.application.message

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.iconect.lightbot.domain.hap.Accessory
import io.iconect.lightbot.domain.hap.AccessoryRepository
import io.iconect.lightbot.domain.hap.service.characteristic.Characteristic
import io.iconect.lightbot.domain.hap.service.characteristic.Permission
import io.iconect.lightbot.domain.hap.service.characteristic.WritableCharacteristic
import io.iconect.lightbot.domain.message.content.HapWritingCharacteristicsMessageContent
import io.iconect.lightbot.infrastructure.message.model.HapStatusCode

class HapWritingCharacteristicsMessageCommand(private val accessoryRepository: AccessoryRepository) : MessageCommand<HapWritingCharacteristicsMessageContent> {

    override fun executeMessage(content: HapWritingCharacteristicsMessageContent): String {
        // find accessories not existing
        val hapWritingCharacteristicErrors = content.content.characteristics
                .filter { accessoryRepository.findByInstanceId(it.aid) == null }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70409.code) }
                .toMutableList()

        // find characteristics not existing
        hapWritingCharacteristicErrors.addAll(content.content.characteristics
                .filter { c -> hapWritingCharacteristicErrors.find { it.aid == c.aid } == null }
                .filter { filterCharacteristic(accessoryRepository.findByInstanceId(it.aid)!!, it.iid) == null }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70409.code) })

        // find characteristics not writable
        hapWritingCharacteristicErrors.addAll(content.content.characteristics
                .filter { c -> hapWritingCharacteristicErrors.find { it.aid == c.aid } == null }
                .filter { !filterCharacteristic(accessoryRepository.findByInstanceId(it.aid)!!, it.iid)!!.permissions.contains(Permission.PAIRED_WRITE) }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70404.code) })

        val hapWritingSuccesses = mutableListOf<HapWritingCharacteristicSuccess>()

        // write existing and writable characteristics
        content.content.characteristics
                .filter { c -> hapWritingCharacteristicErrors.find { it.aid == c.aid } == null }
                .forEach {
                    val accessory = accessoryRepository.findByInstanceId(it.aid)!!
                    val characteristic = filterCharacteristic(accessory, it.iid)
                    if (characteristic is WritableCharacteristic) {
                        try {
                            characteristic.adjustValue(it.value)
                            hapWritingSuccesses.add(HapWritingCharacteristicSuccess(it.aid, it.iid, it.value))
                            // TODO update repository?
                        } catch (e: IllegalArgumentException) {
                            // TODO test me and check status code (i.e. minimum and maximum value)
                            hapWritingCharacteristicErrors.add(HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70404.code))
                        }
                    } else {
                        // TODO test me and check status code
                        hapWritingCharacteristicErrors.add(HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70404.code))
                    }
                }

        if (hapWritingCharacteristicErrors.isNotEmpty()) {
            val mapper = jacksonObjectMapper()
            return mapper.writeValueAsString(HapWritingCharacteristicErrors(hapWritingCharacteristicErrors))
        } else {
            val mapper = jacksonObjectMapper()
            return mapper.writeValueAsString(HapWritingCharacteristicSuccesses(hapWritingSuccesses))
        }
    }

    private fun filterCharacteristic(accessory: Accessory, iid: Int): Characteristic? {
        return accessory.services
                .flatMap { it.characteristics }
                .find { it.instanceId == iid }
    }

    data class HapWritingCharacteristicErrors(val characteristics: List<HapWritingCharacteristicError>)
    data class HapWritingCharacteristicError(val aid: Int, val iid: Int, val status: Int)

    data class HapWritingCharacteristicSuccesses(val characteristics: List<HapWritingCharacteristicSuccess>)
    data class HapWritingCharacteristicSuccess(val aid: Int, val iid: Int, val value: String)
}