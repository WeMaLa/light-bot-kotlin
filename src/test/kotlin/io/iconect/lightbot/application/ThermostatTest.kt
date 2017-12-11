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
                .heaters(listOf(
                        Heater.Builder("heater-1").build(),
                        Heater.Builder("heater-2").build(),
                        Heater.Builder("heater-3").build()
                ))
                .build())

        thermostat.adjust("heater-1", 23)
        thermostat.adjust("heater-2", 0)
        thermostat.adjust("heater-3", 60)
        assertThat(roomRepository.find("room-identifier")?.heaters?.find { h -> h.identifier == "heater-1" }?.degree).isEqualTo(23)
        assertThat(roomRepository.find("room-identifier")?.heaters?.find { h -> h.identifier == "heater-2" }?.degree).isEqualTo(0)
        assertThat(roomRepository.find("room-identifier")?.heaters?.find { h -> h.identifier == "heater-3" }?.degree).isEqualTo(60)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust degree lower than zero`() {
        roomRepository.store(Room.Builder("room-identifier")
                .heaters(listOf(Heater.Builder("heater-1").build()))
                .build())

       thermostat.adjust("heater-1", -1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust degree higher than 60`() {
        roomRepository.store(Room.Builder("room-identifier")
                .heaters(listOf(Heater.Builder("heater-1").build()))
                .build())

        thermostat.adjust("heater-1", 61)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust degree with room not found`() {
        thermostat.adjust("heater-1", 23)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `adjust degree with heater not found`() {
        roomRepository.store(Room.Builder("room-identifier").build())

        thermostat.adjust("heater-1", 23)
    }
}