package io.iconect.lightbot.domain.hap.service.characteristic

// page 148
data class CurrentTemperature(override val instanceId: Int, override val accessoryInstanceId: Int) : Characteristic {

    override val uuid = "00000011-0000-1000-8000-0026BB765291"
    override var value: String = "0.0"
        private set
    override val type = "public.hap.characteristic.temperature.current"
    override val description: String? = null
    override val permissions = listOf(Permission.PAIRED_READ, Permission.NOTIFY)
    override val format = Format.FLOAT
    override val unit: Unit? = Unit.CELSIUS
    override val minimumValue: Double = 0.0
    override val maximumValue: Double = 100.0
    override val stepValue: Double = 0.1
    override val maximumLength: Int? = null
    override val maxDataLength: Int? = null

    fun adjustValue(degree: Double) {
        // TODO validate step
        if (degree < minimumValue || degree > maximumValue) {
            throw IllegalArgumentException("Celcius value $degree must be in range between $minimumValue and $maximumValue")
        }
        value = degree.toString()
    }
}