package io.iconect.lightbot.application.message

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.iconect.lightbot.domain.hap.AccessoryRepository
import io.iconect.lightbot.domain.message.content.HapWritingCharacteristicsMessageContent
import io.iconect.lightbot.infrastructure.message.model.HapStatusCode

class HapWritingCharacteristicsMessageCommand(private val accessoryRepository: AccessoryRepository) : MessageCommand<HapWritingCharacteristicsMessageContent> {

    override fun executeMessage(content: HapWritingCharacteristicsMessageContent): String {
        val notFoundAccessories = content.content.characteristics
                .filter { accessoryRepository.findByInstanceId(it.aid) == null }
                .map { HapWritingCharacteristicError(it.aid, it.iid, HapStatusCode.C70409.code) }

        if (notFoundAccessories.isNotEmpty()) {
            val mapper = jacksonObjectMapper()
            return mapper.writeValueAsString(HapWritingCharacteristicErrors(notFoundAccessories))
        }

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    data class HapWritingCharacteristicErrors(val characteristics: List<HapWritingCharacteristicError>)

    data class HapWritingCharacteristicError(val aid: Int, val iid: Int, val status: Int)
}