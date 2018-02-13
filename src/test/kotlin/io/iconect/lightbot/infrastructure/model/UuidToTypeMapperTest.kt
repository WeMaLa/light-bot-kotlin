package io.iconect.lightbot.infrastructure.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UuidToTypeMapperTest {

    @Test
    fun `map default`() {
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("0000004A-0000-1000-8000-0026BB765291")).isEqualTo("4A")
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("00000011-0000-1000-8000-0026BB765291")).isEqualTo("11")
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("00000035-0000-1000-8000-0026BB765291")).isEqualTo("35")
    }

    @Test
    fun `map incomplete sequence`() {
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("0000004A")).isEqualTo("4A")
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("0000004A-")).isEqualTo("4A")
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("00000011-0000")).isEqualTo("11")
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("00000035-0000-100")).isEqualTo("35")
    }

    @Test
    fun `map no leading sequence`() {
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("4A")).isEqualTo("4A")
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("4A-")).isEqualTo("4A")
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("11-0000")).isEqualTo("11")
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("35-0000-100")).isEqualTo("35")
        assertThat(reduceUuidToTheFirstPartAndRemoveLeadingZeros("35-0000-1000-8000-0026BB765291")).isEqualTo("35")
    }
}