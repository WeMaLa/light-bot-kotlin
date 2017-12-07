package io.iconect.lightbot.domain

class Heater private constructor(val identifier: String, val designation: String, var degree: Short, val maxDegree: Short = 60) {

    fun heatTo(degree: Short) {
        this.degree = degree
    }

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

        fun build(): Heater {
            return Heater(identifier, designation, degree)
        }
    }
}