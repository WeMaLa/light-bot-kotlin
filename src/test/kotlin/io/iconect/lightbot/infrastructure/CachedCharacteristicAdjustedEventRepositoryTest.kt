package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.hap.CharacteristicAdjustedEvent
import io.iconect.lightbot.domain.hap.CharacteristicAdjustedEventRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CachedCharacteristicAdjustedEventRepositoryTest {

    private val repository: CharacteristicAdjustedEventRepository = CachedCharacteristicAdjustedEventRepository()

    @Test
    fun `pop not existing event`() {
        assertThat(repository.popNextEvent()).isNull()
    }

    @Test
    fun `push event`() {
        val event = CharacteristicAdjustedEvent(1, 2, "unit-test-event-value")
        repository.pushEvent(event)

        assertThat(repository.popNextEvent()).isEqualTo(event)
    }

    @Test
    fun `push event and check event is only popped once`() {
        val event = CharacteristicAdjustedEvent(1, 2, "unit-test-event-value")
        repository.pushEvent(event)

        assertThat(repository.popNextEvent()).isEqualTo(event)
        assertThat(repository.popNextEvent()).isNull()
    }

    @Test
    fun `push events and check events are popped in correct order`() {
        val event1 = CharacteristicAdjustedEvent(1, 2, "unit-test-event-value-1")
        val event2 = CharacteristicAdjustedEvent(3, 4, "unit-test-event-value-2")
        val event3 = CharacteristicAdjustedEvent(5, 6, "unit-test-event-value-3")
        repository.pushEvent(event1)
        repository.pushEvent(event2)
        repository.pushEvent(event3)

        assertThat(repository.popNextEvent()).isEqualTo(event1)
        assertThat(repository.popNextEvent()).isEqualTo(event2)
        assertThat(repository.popNextEvent()).isEqualTo(event3)
        assertThat(repository.popNextEvent()).isNull()
    }

    @Test
    fun `clear`() {
        val event = CharacteristicAdjustedEvent(1, 2, "unit-test-event-value")
        repository.pushEvent(event)
        repository.clear()

        assertThat(repository.popNextEvent()).isNull()
    }
}