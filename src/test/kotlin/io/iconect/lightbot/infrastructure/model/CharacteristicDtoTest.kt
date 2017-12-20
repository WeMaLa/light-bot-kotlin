package io.iconect.lightbot.infrastructure.model

import io.iconect.lightbot.domain.service.characteristic.Name
import org.assertj.core.api.Assertions
import org.junit.Test

class CharacteristicDtoTest {

    @Test
    fun `map domain model to dto`() {
        val name = Name(37)

        val nameDto = CharacteristicDto.from(name)

        Assertions.assertThat(nameDto.iid).isEqualTo(37)
        Assertions.assertThat(nameDto.type).isEqualTo("23")
    }

}