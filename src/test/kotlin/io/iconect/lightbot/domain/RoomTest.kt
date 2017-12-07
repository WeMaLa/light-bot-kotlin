package io.iconect.lightbot.domain

import org.assertj.core.api.Assertions
import org.junit.Test

class RoomTest {

    @Test
    fun `check default build`() {
        val room = Room.Builder("room-identifier").build()

        Assertions.assertThat(room.identifier).isEqualTo("room-identifier")
        Assertions.assertThat(room.designation).isEmpty()
        Assertions.assertThat(room.designation).isNotNull()
    }

    @Test
    fun `check build with designation`() {
        val room = Room.Builder("room-identifier")
                .designation("room-test-designation")
                .build()

        Assertions.assertThat(room.identifier).isEqualTo("room-identifier")
        Assertions.assertThat(room.designation).isEqualTo("room-test-designation")
    }

    @Test
    fun `check build with heat up`() {
        val room = Room.Builder("room-identifier")
                .heatTo(23)
                .build()

        Assertions.assertThat(room.identifier).isEqualTo("room-identifier")
        Assertions.assertThat(room.designation).isEmpty()
        Assertions.assertThat(room.designation).isNotNull()
    }
}