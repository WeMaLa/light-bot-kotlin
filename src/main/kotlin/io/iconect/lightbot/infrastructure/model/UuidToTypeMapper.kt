package io.iconect.lightbot.infrastructure.model

fun reduceUuidToTheFirstPartAndRemoveLeadingZeros(uuid: String): String {
    return uuid.split("-")[0].trimStart('0')
}