package chat.to.lightbot.domain.hap.service.characteristic

// page 170
data class On(override val instanceId: Int,
              override val accessoryInstanceId: Int,
              private val eventPublisher: (accessoryInstanceId: Int, characteristicInstanceId: Int, value: String) -> kotlin.Unit) : WritableCharacteristic {

    override val uuid = "00000025-0000-1000-8000-0026BB765291"
    override var value: String = "off"
        private set
    override val type = "public.hap.characteristic.on"
    override val description: String? = null
    override val permissions = listOf(Permission.PAIRED_READ, Permission.PAIRED_WRITE, Permission.NOTIFY)
    override val format = Format.BOOL
    override val unit: Unit? = null
    override val minimumValue: Double? = null
    override val maximumValue: Double? = null
    override val stepValue: Double? = null
    override val maximumLength: Int? = null
    override val maxDataLength: Int? = null

    override fun adjustValue(value: String) {
        if (value != "on" && value != "off") {
            throw IllegalArgumentException("Value $value must be of type $format ('on' or 'off')")
        }

        if (this.value != value) {
            this.eventPublisher(accessoryInstanceId, instanceId, value)
        }
        this.value = value
    }
}