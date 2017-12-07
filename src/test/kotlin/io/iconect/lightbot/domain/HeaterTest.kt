package io.iconect.lightbot.domain

import org.assertj.core.api.Assertions
import org.junit.Test

class HeaterTest {

    @Test
    fun `check default build`() {
        val heater = Heater.Builder("heater-identifier").build()

        Assertions.assertThat(heater.identifier).isEqualTo("heater-identifier")
        Assertions.assertThat(heater.degree).isEqualTo(0)
        Assertions.assertThat(heater.maxDegree).isEqualTo(60)
        Assertions.assertThat(heater.designation).isEmpty()
        Assertions.assertThat(heater.designation).isNotNull()
    }

    @Test
    fun `check build with designation`() {
        val heater = Heater.Builder("heater-identifier")
                .designation("heater-test-designation")
                .build()

        Assertions.assertThat(heater.identifier).isEqualTo("heater-identifier")
        Assertions.assertThat(heater.degree).isEqualTo(0)
        Assertions.assertThat(heater.designation).isEqualTo("heater-test-designation")
    }

    @Test
    fun `check build with heat up`() {
        val heater = Heater.Builder("heater-identifier")
                .heatTo(23)
                .build()

        Assertions.assertThat(heater.identifier).isEqualTo("heater-identifier")
        Assertions.assertThat(heater.degree).isEqualTo(23)
        Assertions.assertThat(heater.designation).isEmpty()
        Assertions.assertThat(heater.designation).isNotNull()
    }

    @Test
    fun `switch heater degree`() {
        val heater = Heater.Builder("heater-identifier").build()
        Assertions.assertThat(heater.identifier).isEqualTo("heater-identifier")
        Assertions.assertThat(heater.degree).isEqualTo(0)

        heater.heatTo(19)
        Assertions.assertThat(heater.identifier).isEqualTo("heater-identifier")
        Assertions.assertThat(heater.degree).isEqualTo(19)

        heater.heatTo(23)
        Assertions.assertThat(heater.identifier).isEqualTo("heater-identifier")
        Assertions.assertThat(heater.degree).isEqualTo(23)
    }
}