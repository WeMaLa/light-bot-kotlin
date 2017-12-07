package io.iconect.lightbot.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LampTest {

    @Test
    fun `check default build`() {
        val lamp = Lamp.Builder("lamp-identifier").build()

        assertThat(lamp.identifier).isEqualTo("lamp-identifier")
        assertThat(lamp.shines).isFalse()
        assertThat(lamp.designation).isEmpty()
        assertThat(lamp.designation).isNotNull()
    }

    @Test
    fun `check build with designation`() {
        val lamp = Lamp.Builder("lamp-identifier")
                .designation("lamp-test-designation")
                .build()

        assertThat(lamp.identifier).isEqualTo("lamp-identifier")
        assertThat(lamp.shines).isFalse()
        assertThat(lamp.designation).isEqualTo("lamp-test-designation")
    }

    @Test
    fun `check build with shines`() {
        val lamp = Lamp.Builder("lamp-identifier")
                .shines(true)
                .build()

        assertThat(lamp.identifier).isEqualTo("lamp-identifier")
        assertThat(lamp.shines).isTrue()
        assertThat(lamp.designation).isEmpty()
        assertThat(lamp.designation).isNotNull()
    }

    @Test
    fun `switch shines`() {
        val lamp = Lamp.Builder("lamp-identifier").build()
        assertThat(lamp.identifier).isEqualTo("lamp-identifier")
        assertThat(lamp.shines).isFalse()

        lamp.switchOn()
        assertThat(lamp.identifier).isEqualTo("lamp-identifier")
        assertThat(lamp.shines).isTrue()

        lamp.switchOff()
        assertThat(lamp.identifier).isEqualTo("lamp-identifier")
        assertThat(lamp.shines).isFalse()
    }
}