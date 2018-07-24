package chat.to.lightbot.domain.hap.service.characteristic

// page 170
data class CurrentPosition(override val instanceId: Int,
                           override val accessoryInstanceId: Int,
                           private val eventPublisher: (accessoryInstanceId: Int, characteristicInstanceId: Int, value: String) -> kotlin.Unit) : Characteristic {

    override val uuid = "0000006D-0000-1000-8000-0026BB765291"
    override var value: String = "0"
        private set
    override val type = "public.hap.characteristic.position.current"
    override val description: String? = null
    override val permissions = listOf(Permission.PAIRED_READ, Permission.NOTIFY)
    override val format = Format.UINT8
    override val unit: Unit? = Unit.PERCENTAGE
    override val minimumValue: Double = 0.0
    override val maximumValue: Double = 100.0
    override val stepValue: Double = 1.0
    override val maximumLength: Int? = null
    override val maxDataLength: Int? = null

    fun adjustValue(percentage: Int) {
        if (percentage < minimumValue.toInt() || percentage > maximumValue.toInt()) {
            throw IllegalArgumentException("Percentage value $percentage must be in range between $minimumValue and $maximumValue")
        }

        val newValue = percentage.toString()
        if (value != newValue) {
            this.eventPublisher(accessoryInstanceId, instanceId, newValue)
        }

        value = newValue
    }
}