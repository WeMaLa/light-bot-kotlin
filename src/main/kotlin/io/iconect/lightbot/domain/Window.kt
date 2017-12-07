package io.iconect.lightbot.domain

data class Window constructor(val identifier: String, val designation: String) {

    var opened: Boolean = false
        private set

    fun open() {
        this.opened = true
    }

    fun close() {
        this.opened = false
    }

    class Builder(private val identifier: String) {
        private var designation: String = ""
        private var openend: Boolean = false

        fun designation(designation: String): Builder {
            this.designation = designation
            return this
        }

        fun opened(shines: Boolean): Builder {
            this.openend = shines
            return this
        }

        fun build(): Window {
            val window = Window(identifier, designation)
            window.opened = openend
            return window
        }
    }
}