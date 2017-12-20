package io.iconect.lightbot.infrastructure.model

import io.iconect.lightbot.domain.service.characteristic.Name
import org.assertj.core.api.Assertions
import org.junit.Test

class CharacteristicDtoTest {

    @Test
    fun `map domain model to dto`() {
        val name = Name(37)
        name.updateName("unit-test-value-name")

        val nameDto = CharacteristicDto.from(name)

        Assertions.assertThat(nameDto.iid).isEqualTo(37)
        Assertions.assertThat(nameDto.type).isEqualTo("23")
        Assertions.assertThat(nameDto.value).isEqualTo("unit-test-value-name")
    }

}