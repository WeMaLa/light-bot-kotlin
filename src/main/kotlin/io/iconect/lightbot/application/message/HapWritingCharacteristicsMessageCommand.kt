package io.iconect.lightbot.application.message

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.iconect.lightbot.domain.hap.Accessory
import io.iconect.lightbot.domain.hap.AccessoryRepository
import io.iconect.lightbot.domain.hap.service.characteristic.Characteristic
import io.iconect.lightbot.domain.message.content.HapWritingCharacteristicsMessageContent
import io.iconect.lightbot.infrastructure.message.model.HapStatusCode

class HapWritingCharacteristicsMessageCommand(private val accessoryRepository: AccessoryRepository) : MessageCommand<HapWritingCharacteristicsMessageContent> {

    override fun executeMessage(content: HapWritingCharacteristicsMessageContent): String {
        val hapWritingCharacteristicErrors = content.content.characteristics
                .filter { accessoryRepository.findByInstanceId(it.aid) == null }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70409.code) }
                .toMutableList()

        hapWritingCharacteristicErrors.addAll(content.content.characteristics
                .filter { c -> hapWritingCharacteristicErrors.find { it.aid == c.aid } == null }
                .filter { filterCharacteristic(accessoryRepository.findByInstanceId(it.aid)!!, it.iid) == null }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70409.code) })

        if (hapWritingCharacteristicErrors.isNotEmpty()) {
            val mapper = jacksonObjectMapper()
            return mapper.writeValueAsString(HapWritingCharacteristicErrors(hapWritingCharacteristicErrors))
        }

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun filterCharacteristic(accessory: Accessory, iid: Int): Characteristic? {
        return accessory.services
                .flatMap { it.characteristics }
                .find { it.instanceId == iid }
    }

    data class HapWritingCharacteristicErrors(val characteristics: List<HapWritingCharacteristicError>)

    data class HapWritingCharacteristicError(val aid: Int, val iid: Int, val status: Int)
}