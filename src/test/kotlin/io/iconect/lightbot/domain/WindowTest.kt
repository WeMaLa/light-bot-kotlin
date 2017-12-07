package io.iconect.lightbot.domain

import org.assertj.core.api.Assertions
import org.junit.Test

class WindowTest {

    @Test
    fun `check default build`() {
        val window = Window.Builder("window-identifier").build()

        Assertions.assertThat(window.identifier).isEqualTo("window-identifier")
        Assertions.assertThat(window.opened).isFalse()
        Assertions.assertThat(window.designation).isEmpty()
        Assertions.assertThat(window.designation).isNotNull()
    }

    @Test
    fun `check build with designation`() {
        val window = Window.Builder("window-identifier")
                .designation("window-test-designation")
                .build()

        Assertions.assertThat(window.identifier).isEqualTo("window-identifier")
        Assertions.assertThat(window.opened).isFalse()
        Assertions.assertThat(window.designation).isEqualTo("window-test-designation")
    }

    @Test
    fun `check build with shines`() {
        val window = Window.Builder("window-identifier")
                .opened(true)
                .build()

        Assertions.assertThat(window.identifier).isEqualTo("window-identifier")
        Assertions.assertThat(window.opened).isTrue()
        Assertions.assertThat(window.designation).isEmpty()
        Assertions.assertThat(window.designation).isNotNull()
    }

    @Test
    fun `switch window open and close`() {
        val window = Window.Builder("window-identifier").build()
        Assertions.assertThat(window.identifier).isEqualTo("window-identifier")
        Assertions.assertThat(window.opened).isFalse()

        window.open()
        Assertions.assertThat(window.identifier).isEqualTo("window-identifier")
        Assertions.assertThat(window.opened).isTrue()

        window.close()
        Assertions.assertThat(window.identifier).isEqualTo("window-identifier")
        Assertions.assertThat(window.opened).isFalse()
    }
}