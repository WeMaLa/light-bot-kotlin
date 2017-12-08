package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
class CachedRoomRepositoryTest {

    @Autowired
    lateinit var roomRepository: RoomRepository

    @Before
    fun setUp() {
        roomRepository.clear()
    }

    @Test
    fun `check loadAll with empty rooms`() {
        assertThat(roomRepository.loadAll()).isEmpty()
    }

    @Test
    fun `check store room`() {
        val room = Room.Builder("room-identifier")
                .heaters(listOf(Heater.Builder("heater-1").build(), Heater.Builder("heater-2").build()))
                .windows(listOf(Window.Builder("window-1").build(), Window.Builder("window-2").build()))
                .lamps(listOf(Lamp.Builder("lamp-1").build(), Lamp.Builder("lamp-2").build()))
                .build()

        assertThat(roomRepository.loadAll()).isEmpty()

        roomRepository.store(room)

        assertThat(roomRepository.loadAll()).containsExactly(room)
    }

}