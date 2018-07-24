package chat.to.lightbot.infrastructure

import chat.to.lightbot.domain.hap.CharacteristicAdjustedEvent
import chat.to.lightbot.domain.hap.CharacteristicAdjustedEventRepository
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