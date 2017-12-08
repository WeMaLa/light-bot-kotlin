package io.iconect.lightbot.application

import io.iconect.lightbot.domain.RoomRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("unittest")
class RoomInitializerTest {

    @Autowired
    lateinit var roomInitializer: RoomInitializer

    @Autowired
    lateinit var roomRepository: RoomRepository

    @Test
    fun `check rooms are initialized`() {
        roomInitializer.initializeRooms()

        val rooms = roomRepository.loadAll()
        val windows = rooms.flatMap { r -> r.windows }
        val heaters = rooms.flatMap { r -> r.heaters }
        val lamps = rooms.flatMap { r -> r.lamps }

        assertThat(rooms)
                .extracting("identifier", "designation")
                .containsOnly(
                        tuple("kitchen-1", "kitchen")
                )

        assertThat(windows.first { w -> w.identifier ==  "kitchen-1-window-1"}.designation).isEqualTo("window left")
        assertThat(windows.first { w -> w.identifier ==  "kitchen-1-window-1"}.opened).isFalse()
        assertThat(windows.first { w -> w.identifier ==  "kitchen-1-window-2"}.designation).isEqualTo("window right")
        assertThat(windows.first { w -> w.identifier ==  "kitchen-1-window-2"}.opened).isFalse()

        assertThat(heaters.first { w -> w.identifier ==  "kitchen-1-heater-1"}.designation).isEqualTo("floor heating")
        assertThat(heaters.first { w -> w.identifier ==  "kitchen-1-heater-1"}.degree).isEqualTo(0)

        assertThat(lamps.first { w -> w.identifier ==  "kitchen-1-lamp-1"}.designation).isEqualTo("overhead lights")
        assertThat(lamps.first { w -> w.identifier ==  "kitchen-1-lamp-1"}.shines).isFalse()
    }
}