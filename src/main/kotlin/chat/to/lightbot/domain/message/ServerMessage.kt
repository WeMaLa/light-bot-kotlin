package chat.to.lightbot.domain.message

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import chat.to.lightbot.infrastructure.message.model.HapStatusCode

data class ServerMessage internal constructor(
        val raw: String,
        val channel: String,
        val type: ServerMessageType = ServerMessageType.UNKNOWN,
        val content: Any = Unit) {

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

    class JsonConvertException(text: String) : RuntimeException(text)

}