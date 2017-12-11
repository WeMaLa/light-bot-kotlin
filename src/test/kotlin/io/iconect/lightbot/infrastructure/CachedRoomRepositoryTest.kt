package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
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
    fun `find all rooms but repository is empty`() {
        assertThat(roomRepository.findAll()).isEmpty()
    }

    @Test
    fun `store room`() {
        val room = Room.Builder("room-identifier")
                .heaters(listOf(Heater.Builder("heater-1").build(), Heater.Builder("heater-2").build()))
                .windows(listOf(Window.Builder("window-1").build(), Window.Builder("window-2").build()))
                .lamps(listOf(Lamp.Builder("lamp-1").build(), Lamp.Builder("lamp-2").build()))
                .build()

        assertThat(roomRepository.findAll()).isEmpty()

        roomRepository.store(room)

        assertThat(roomRepository.findAll()).containsExactly(room)
    }

    @Test
    fun `update stored room`() {
        val room = Room.Builder("room-identifier")
                .designation("room-designation")
                .heaters(listOf(Heater.Builder("heater-1").build(), Heater.Builder("heater-2").build()))
                .windows(listOf(Window.Builder("window-1").build(), Window.Builder("window-2").build()))
                .lamps(listOf(Lamp.Builder("lamp-1").build(), Lamp.Builder("lamp-2").build()))
                .build()

        roomRepository.store(room)

        assertThat(roomRepository.find("room-identifier")?.identifier).isEqualTo("room-identifier")
        assertThat(roomRepository.find("room-identifier")?.designation).isEqualTo("room-designation")
        assertThat(roomRepository.find("room-identifier")?.lamps)
                .extracting("identifier", "shines")
                .containsOnly(
                        tuple("lamp-1", false),
                        tuple("lamp-2", false)
                )
        assertThat(roomRepository.find("room-identifier")?.windows)
                .extracting("identifier", "opened")
                .containsOnly(
                        tuple("window-1", false),
                        tuple("window-2", false)
                )
        assertThat(roomRepository.find("room-identifier")?.heaters)
                .extracting("identifier", "degree", "maxDegree")
                .containsOnly(
                        tuple("heater-1", 0.toShort(), 60.toShort()),
                        tuple("heater-2", 0.toShort(), 60.toShort())
                )

        val updatedRoom = Room.Builder("room-identifier")
                .designation("room-designation")
                .heaters(listOf(Heater.Builder("heater-1").build(), Heater.Builder("heater-2").build()))
                .windows(listOf(Window.Builder("window-1").build(), Window.Builder("window-2").build()))
                .lamps(listOf(Lamp.Builder("lamp-1").build(), Lamp.Builder("lamp-2").build()))
                .build()
        updatedRoom.lamps.find { l -> l.identifier ==  "lamp-1"}?.switchOn()
        updatedRoom.windows.find { l -> l.identifier ==  "window-2"}?.open()
        updatedRoom.heaters.find { l -> l.identifier ==  "heater-1"}?.heatTo(23)
        updatedRoom.heaters.find { l -> l.identifier ==  "heater-2"}?.heatTo(19)

        roomRepository.store(updatedRoom)

        assertThat(roomRepository.findAll().size).isEqualTo(1)

        assertThat(roomRepository.find("room-identifier")?.identifier).isEqualTo("room-identifier")
        assertThat(roomRepository.find("room-identifier")?.designation).isEqualTo("room-designation")
        assertThat(roomRepository.find("room-identifier")?.lamps)
                .extracting("identifier", "shines")
                .containsOnly(
                        tuple("lamp-1", true),
                        tuple("lamp-2", false)
                )
        assertThat(roomRepository.find("room-identifier")?.windows)
                .extracting("identifier", "opened")
                .containsOnly(
                        tuple("window-1", false),
                        tuple("window-2", true)
                )
        assertThat(roomRepository.find("room-identifier")?.heaters)
                .extracting("identifier", "degree", "maxDegree")
                .containsOnly(
                        tuple("heater-1", 23.toShort(), 60.toShort()),
                        tuple("heater-2", 19.toShort(), 60.toShort())
                )

        // TODO update all and store it
    }

    @Test
    fun `find room by identifier with identifier is empty`() {
        assertThat(roomRepository.find("")).isNull()
    }

    @Test
    fun `find room by identifier`() {
        val room1 = Room.Builder("room-identifier-1").build()
        val room2 = Room.Builder("room-identifier-2").build()

        roomRepository.store(room1)
        roomRepository.store(room2)

        assertThat(roomRepository.find("room-identifier-1")).isEqualTo(room1)
        assertThat(roomRepository.find("room-identifier-2")).isEqualTo(room2)
        assertThat(roomRepository.find("room-not-found")).isNull()
    }

}