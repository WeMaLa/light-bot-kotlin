package io.iconect.lightbot.domain.message.content

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.iconect.lightbot.domain.message.content.exception.JsonConvertException
import io.iconect.lightbot.infrastructure.message.model.HapStatusCode

class HapWritingCharacteristicsMessageContent(override val message: String, override val content: HapWritingCharacteristics) : MessageContent<HapWritingCharacteristicsMessageContent.HapWritingCharacteristics> {

    data class HapWritingCharacteristics(val characteristics: List<HapWritingCharacteristic>) {

        data class HapWritingCharacteristic(val aid: Int, val iid: Int, val value: String)

        companion object {

            @Throws(JsonConvertException::class)
            fun from(json: String) : HapWritingCharacteristics {
                try {
                    val mapper = jacksonObjectMapper()
                    return mapper.readValue(json)
                } catch (e: Exception) {
                    throw JsonConvertException("Could not convert '$json' to HapWritingCharacteristics. Status code is '${HapStatusCode.C70410}'")
                }
            }
        }

    }

}