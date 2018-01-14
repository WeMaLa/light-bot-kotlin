package io.iconect.lightbot.domain.hap

interface CharacteristicAdjustedEventRepository {

    fun popNextEvent(): CharacteristicAdjustedEvent?

    fun pushEvent(event: CharacteristicAdjustedEvent)

    // TODO only used in unit test. Find a better way
    fun clear()
}