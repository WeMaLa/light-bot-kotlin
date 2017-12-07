package io.iconect.lightbot.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RoomTest {

    @Test
    fun `check default build`() {
        val room = Room.Builder("room-identifier").build()

        assertThat(room.identifier).isEqualTo("room-identifier")
        assertThat(room.designation).isEmpty()
        assertThat(room.designation).isNotNull()
    }

    @Test
    fun `check build with designation`() {
        val room = Room.Builder("room-identifier")
                .designation("room-test-designation")
                .build()

        assertThat(room.identifier).isEqualTo("room-identifier")
        assertThat(room.designation).isEqualTo("room-test-designation")
    }

    @Test
    fun `check build with heaters`() {
        val room = Room.Builder("room-identifier")
                .heaters(listOf(Heater.Builder("heater-1").build(), Heater.Builder("heater-2").build()))
                .build()

        assertThat(room.identifier).isEqualTo("room-identifier")
        assertThat(room.designation).isEmpty()
        assertThat(room.designation).isNotNull()
        assertThat(room.heaters)
                .extracting("identifier")
                .containsExactlyInAnyOrder("heater-1", "heater-2")
    }

    @Test
    fun `check build with windows`() {
        val room = Room.Builder("room-identifier")
                .windows(listOf(Window.Builder("window-1").build(), Window.Builder("window-2").build()))
                .build()

        assertThat(room.identifier).isEqualTo("room-identifier")
        assertThat(room.designation).isEmpty()
        assertThat(room.designation).isNotNull()
        assertThat(room.windows)
                .extracting("identifier")
                .containsExactlyInAnyOrder("window-1", "window-2")
    }

    @Test
    fun `check build with lamps`() {
        val room = Room.Builder("room-identifier")
                .lamps(listOf(Lamp.Builder("lamp-1").build(), Lamp.Builder("lamp-2").build()))
                .build()

        assertThat(room.identifier).isEqualTo("room-identifier")
        assertThat(room.designation).isEmpty()
        assertThat(room.designation).isNotNull()
        assertThat(room.lamps)
                .extracting("identifier")
                .containsExactlyInAnyOrder("lamp-1", "lamp-2")
    }
}