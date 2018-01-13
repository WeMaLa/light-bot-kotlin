package io.iconect.lightbot.infrastructure.model

import io.iconect.lightbot.domain.hap.Accessory
import io.iconect.lightbot.domain.hap.service.Thermostat
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AccessoryDtoTest {

    @Test
    fun `map domain model to dto`() {
        val thermostat = Thermostat(11, 1,12, 13, 14)
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