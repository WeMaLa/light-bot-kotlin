package io.iconect.lightbot.infrastructure.message.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class HapWritingCharacteristics(val characteristics: List<HapWritingCharacteristic>) {

    data class HapWritingCharacteristic(val aid: Int, val iid: Int, val value: String)

    companion object {
        fun from(json: String) : HapWritingCharacteristics {
            val mapper = jacksonObjectMapper()
            return mapper.readValue(json)
        }
    }

}