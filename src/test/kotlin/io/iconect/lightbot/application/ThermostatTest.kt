package io.iconect.lightbot.application

import io.iconect.lightbot.domain.Heater
import io.iconect.lightbot.domain.Room
import io.iconect.lightbot.domain.RoomRepository
import io.iconect.lightbot.infrastructure.CachedRoomRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ThermostatTest {

    private val roomRepository: RoomRepository = CachedRoomRepository()
    private val thermostat: Thermostat = Thermostat(roomRepository)

    @Test
    fun `adjust degree`() {
        roomRepository.store(Room.Builder("room-identifier")
                .heaters(listOf(Heater.Builder("heater-1").build()))
                .build())

        assertThat(thermostat.adjust("heater-1", 23)).isTrue()
        assertThat(roomRepository.find("room-identifier")?.heaters?.find { h -> h.identifier == "heater-1" }?.degree).isEqualTo(23)
    }

    @Test
    fun `adjust degree with room not found`() {
        assertThat(thermostat.adjust("heater-1", 23)).isFalse()
    }

    @Test
    fun `adjust degree with heater not found`() {
        roomRepository.store(Room.Builder("room-identifier").build())

        assertThat(thermostat.adjust("heater-1", 23)).isFalse()
    }
}