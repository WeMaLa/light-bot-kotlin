package chat.to.lightbot.infrastructure.model

import chat.to.lightbot.domain.hap.Accessory
import chat.to.lightbot.domain.hap.service.Thermostat
import chat.to.lightbot.infrastructure.model.AccessoryDto
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AccessoryDtoTest {

    @Test
    fun `map domain model to dto`() {
        val thermostat = Thermostat(11, 1, 12, 13, 14, { _, _, _ -> })
        val accessory = Accessory(1, listOf(thermostat))

        val accessoryDto = AccessoryDto.from(accessory)

        assertThat(accessoryDto.aid).isEqualTo(1)
        assertThat(accessoryDto.services.size).isEqualTo(1)

        Assertions.assertThat(accessoryDto.services)
                .extracting("type", "iid")
                .containsExactly(
                        Assertions.tuple("4A", 11)
                )
    }

    @Test
    fun `map domain model to dto with empty services`() {
        val accessory = Accessory(1, emptyList())

        val accessoryDto = AccessoryDto.from(accessory)

        assertThat(accessoryDto.aid).isEqualTo(1)
        assertThat(accessoryDto.services.size).isEqualTo(0)
    }

}