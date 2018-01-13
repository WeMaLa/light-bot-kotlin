package io.iconect.lightbot.domain.hap

interface CharacteristicAdjustedEventRepository {

    fun popNextEvent(): CharacteristicAdjustedEvent?

    fun pushEvent(event: CharacteristicAdjustedEvent)

}