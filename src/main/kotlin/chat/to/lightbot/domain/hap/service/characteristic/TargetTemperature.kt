package chat.to.lightbot.domain.hap.service.characteristic

// page 162
data class TargetTemperature(override val instanceId: Int,
                             override val accessoryInstanceId: Int,
                             private val eventPublisher: (accessoryInstanceId: Int, characteristicInstanceId: Int, value: String) -> kotlin.Unit) : WritableCharacteristic {

    override val uuid = "00000035-0000-1000-8000-0026BB765291"
    override var value: String = "10.0" // TODO use a generic in interface?
        private set
    override val type = "public.hap.characteristic.temperature.target"
    override val description: String? = null
    override val permissions = listOf(Permission.PAIRED_READ, Permission.PAIRED_WRITE, Permission.NOTIFY)
    override val format = Format.FLOAT
    override val unit: Unit? = Unit.CELSIUS
    override val minimumValue: Double = 10.0
    override val maximumValue: Double = 38.0
    override val stepValue: Double? = null
    override val maximumLength: Int? = null
    override val maxDataLength: Int? = null

    override fun adjustValue(value: String) {
        try {
            val degree = value.toDouble()
            if (degree < minimumValue || degree > maximumValue) {
                throw IllegalArgumentException("Celcius value $value must be in range between $minimumValue and $maximumValue")
            }

            if (this.value != value) {
                eventPublisher(accessoryInstanceId, instanceId, value)
            }

            this.value = value
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Celcius value $value must be of type $format")
        }
    }
}