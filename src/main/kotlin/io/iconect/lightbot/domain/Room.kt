package io.iconect.lightbot.domain

data class Room constructor(val identifier: String, val designation: String) {

    var heaters: List<Heater> = listOf()
        private set
    var lamps: List<Lamp> = listOf()
        private set
    var windows: List<Window> = listOf()
        private set

    private fun addHeater(heater: Heater) {
        heaters = combineItems(heaters, heater)
    }

    private fun addWindows(window: Window) {
        windows = combineItems(windows, window)
    }

    private fun addWLamps(lamp: Lamp) {
        lamps = combineItems(lamps, lamp)
    }

    private fun <T> combineItems(items: List<T>, item: T) : List<T> {
        val newWindows = items.toMutableList()
        newWindows.add(item);
        return newWindows.toList()
    }

    class Builder(private val identifier: String) {
        private var designation: String = ""
        private var heaters: List<Heater> = listOf()
        private var windows: List<Window> = listOf()
        private var lamps: List<Lamp> = listOf()

        fun designation(designation: String): Builder {
            this.designation = designation
            return this
        }

        fun heaters(heaters: List<Heater>): Builder {
            this.heaters = heaters
            return this
        }

        fun windows(windows: List<Window>): Builder {
            this.windows = windows
            return this
        }

        fun lamps(lamps: List<Lamp>): Builder {
            this.lamps = lamps
            return this
        }

        fun build(): Room {
            val room = Room(identifier, designation)
            heaters.forEach { h -> room.addHeater(h) }
            windows.forEach { w -> room.addWindows(w) }
            lamps.forEach { l -> room.addWLamps(l) }
            return room
        }
    }
}