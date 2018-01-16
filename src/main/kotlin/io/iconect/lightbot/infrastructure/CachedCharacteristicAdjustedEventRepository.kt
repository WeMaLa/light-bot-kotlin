package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.hap.CharacteristicAdjustedEvent
import io.iconect.lightbot.domain.hap.CharacteristicAdjustedEventRepository
import org.springframework.stereotype.Repository

@Repository
class CachedCharacteristicAdjustedEventRepository : CharacteristicAdjustedEventRepository {

    private val events = mutableListOf<CharacteristicAdjustedEvent>()

    override fun popNextEvent(): CharacteristicAdjustedEvent? {
        return if (events.size > 0) {
            val event = events[0]
            events.removeAt(0)
            event
        } else {
            null
        }
    }

    override fun pushEvent(event: CharacteristicAdjustedEvent) {
        events.add(event)
    }

    override fun clear() {
        events.clear()
    }
}