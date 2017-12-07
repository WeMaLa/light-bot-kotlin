package io.iconect.lightbot.domain

class Window private constructor(val identifier: String, var designation: String, var opened: Boolean = false) {

    fun open() {
        this.opened = true
    }

    fun close() {
        this.opened = false
    }

    class Builder(private val identifier: String) {
        private var designation: String = ""
        private var shines: Boolean = false

        fun designation(designation: String) : Builder {
            this.designation = designation
            return this
        }

        fun opened(shines: Boolean) : Builder {
            this.shines = shines
            return this
        }

        fun build(): Window {
            return Window(identifier, designation, shines);
        }
    }
}