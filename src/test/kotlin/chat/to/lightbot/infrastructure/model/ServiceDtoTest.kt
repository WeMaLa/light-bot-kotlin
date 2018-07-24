package chat.to.lightbot.infrastructure.model

import chat.to.lightbot.domain.hap.service.Thermostat
import chat.to.lightbot.infrastructure.model.ServiceDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ServiceDtoTest {

    @Test
    fun `map domain model to dto`() {
        val thermostat = Thermostat(11, 1, 12, 13, 14, { _, _, _ -> })

        val serviceDto = ServiceDto.from(thermostat)

        assertThat(serviceDto.iid).isEqualTo(11)
        assertThat(serviceDto.characteristics).extracting("iid").containsExactly(12, 13, 14)

    }

}