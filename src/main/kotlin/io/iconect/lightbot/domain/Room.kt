package io.iconect.lightbot.domain

class Room private constructor(val identifier: String, val designation: String) {

    class Builder(private val identifier: String) {
        private var designation: String = ""
        private var degree: Short = 0

        fun designation(designation: String) : Builder {
            this.designation = designation
            return this
        }

        fun heatTo(degree: Short) : Builder {
            this.degree = degree
            return this
        }

        fun build(): Room {
            return Room(identifier, designation)
        }
    }
}