package io.iconect.lightbot.infrastructure.model

class UuidToTypeMapper {

    companion object {
        fun map(uuid: String): String {
            return uuid.split("-")[0].trimStart('0')
        }
    }

}