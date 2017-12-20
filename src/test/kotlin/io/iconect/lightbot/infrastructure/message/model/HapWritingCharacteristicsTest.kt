package io.iconect.lightbot.infrastructure.message.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.Test

class HapWritingCharacteristicsTest {

    @Test
    fun `from a single characteristic`() {
        val singleWriteCharacteristics = "{" +
                "\"characteristics\" : [\n" +
                "   {\n" +
                "       \"aid\" : 2,\n" +
                "       \"iid\" : 8,\n" +
                "       \"value\" : true\n" +
                "   } " +
                "]}"

        val hapWritingCharacteristics = HapWritingCharacteristics.from(singleWriteCharacteristics)

        assertThat(hapWritingCharacteristics.characteristics.size).isEqualTo(1)
        assertThat(hapWritingCharacteristics.characteristics[0].aid).isEqualTo(2)
        assertThat(hapWritingCharacteristics.characteristics[0].iid).isEqualTo(8)
        assertThat(hapWritingCharacteristics.characteristics[0].value).isEqualTo("true")
    }

    @Test
    fun `from a multiple characteristic`() {
        val singleWriteCharacteristics = "{" +
                "\"characteristics\" : [\n" +
                "   {\n" +
                "       \"aid\" : 2,\n" +
                "       \"iid\" : 8,\n" +
                "       \"value\" : true\n" +
                "   }," +
                "   {\n" +
                "       \"aid\" : 3,\n" +
                "       \"iid\" : 9,\n" +
                "       \"value\" : \"on\"\n" +
                "   } " +
                "]}"

        val hapWritingCharacteristics = HapWritingCharacteristics.from(singleWriteCharacteristics)

        assertThat(hapWritingCharacteristics.characteristics.size).isEqualTo(2)
        assertThat(hapWritingCharacteristics.characteristics)
                .extracting("aid", "iid", "value")
                .containsOnly(
                        tuple(2, 8, "true"),
                        tuple(3, 9, "on")
                )
    }

    @Test
    fun `from with json string could not be unmarshalled`() {
        val incomplete = "{" +
                "\"characteristics\" : [\n" +
                "   {\n" +
                "       \"aid\" : 2,\n" +
                "       \"iid\" : 8,\n" +
                "   } " +
                "]}"

        HapWritingCharacteristics.from(incomplete)
    }
}