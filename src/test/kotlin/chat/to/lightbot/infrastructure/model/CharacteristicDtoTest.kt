package chat.to.lightbot.infrastructure.model

import chat.to.lightbot.domain.hap.service.characteristic.Format
import chat.to.lightbot.domain.hap.service.characteristic.Name
import chat.to.lightbot.domain.hap.service.characteristic.Permission
import chat.to.lightbot.infrastructure.model.CharacteristicDto
import org.assertj.core.api.Assertions
import org.junit.Test

class CharacteristicDtoTest {

    @Test
    fun `map domain model to dto`() {
        val name = Name(37, 1, { _, _, _ -> })
        name.updateName("unit-test-value-name")

        val nameDto = CharacteristicDto.from(name)

        Assertions.assertThat(nameDto.iid).isEqualTo(37)
        Assertions.assertThat(nameDto.type).isEqualTo("23")
        Assertions.assertThat(nameDto.value).isEqualTo("unit-test-value-name")
        Assertions.assertThat(nameDto.format).isEqualTo(Format.STRING.json)
        Assertions.assertThat(nameDto.perms).containsExactly(Permission.PAIRED_READ.json)
    }

}