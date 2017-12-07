package io.iconect.lightbot.domain

data class Heater constructor(val identifier: String, val designation: String, val maxDegree: Short = 60) {

    var degree: Short = 0
        private set

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
            val heater = Heater(identifier, designation)
            heater.degree = degree
            return heater
        }
    }
}