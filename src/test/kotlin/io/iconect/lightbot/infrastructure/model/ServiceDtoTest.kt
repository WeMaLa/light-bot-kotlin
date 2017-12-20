package io.iconect.lightbot.infrastructure.model

import io.iconect.lightbot.domain.service.Thermostat
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ServiceDtoTest {

    @Test
    fun `map domain model to dto`() {
        val thermostat = Thermostat(11, 12, 13, 14)

        val serviceDto = ServiceDto.from(thermostat)

        assertThat(serviceDto.iid).isEqualTo(11)
        assertThat(serviceDto.iid).isEqualTo(11)
        assertThat(serviceDto.characteristics).extracting("iid").containsExactly(12, 13, 14)

    }

}