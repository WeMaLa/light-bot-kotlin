package io.iconect.lightbot.domain

class Lamp private constructor(val identifier: String, val designation: String) {

    var shines: Boolean = false
        private set

    fun switchOn() {
        this.shines = true
    }

    fun switchOff() {
        this.shines = false
    }

    class Builder(private val identifier: String) {
        private var designation: String = ""
        private var shines: Boolean = false

        fun designation(designation: String): Builder {
            this.designation = designation
            return this
        }

        fun shines(shines: Boolean): Builder {
            this.shines = shines
            return this
        }

        fun build(): Lamp {
            val lamp = Lamp(identifier, designation)
            lamp.shines = shines
            return lamp
        }
    }
}