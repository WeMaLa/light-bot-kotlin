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

        assertThat(roomRepository.loadAll())
                .extracting("identifier", "designation")
                .containsOnly(
                        tuple("kitchen-1", "kitchen")
                )
    }
}