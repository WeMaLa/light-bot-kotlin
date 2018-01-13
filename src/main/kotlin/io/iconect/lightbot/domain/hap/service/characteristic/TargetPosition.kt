package io.iconect.lightbot.domain.hap.service.characteristic

// page 170
data class TargetPosition(override val instanceId: Int,
                          override val accessoryInstanceId: Int,
                          private val eventPublisher: (accessoryInstanceId: Int, characteristicInstanceId: Int, value: String) -> kotlin.Unit) : WritableCharacteristic {

    override val uuid = "0000007C-0000-1000-8000-0026BB765291"
    override var value: String = "0"
        private set
    override val type = "public.hap.characteristic.position.target"
    override val description: String? = null
    override val permissions = listOf(Permission.PAIRED_READ, Permission.PAIRED_WRITE, Permission.NOTIFY)
    override val format = Format.UINT8
    override val unit: Unit? = Unit.PERCENTAGE
    override val minimumValue: Double = 0.0
    override val maximumValue: Double = 100.0
    override val stepValue: Double = 1.0
    override val maximumLength: Int? = null
    override val maxDataLength: Int? = null

    override fun adjustValue(value: String) {
        try {
            val percentage = value.toInt()
            if (percentage < minimumValue || percentage > maximumValue) {
                throw IllegalArgumentException("Percentage value $value must be in range between $minimumValue and $maximumValue")
            }

            if (this.value != value) {
                eventPublisher(accessoryInstanceId, instanceId, value)
            }

            this.value = value
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Percentage value $value must be of type $format")
        }
    }
}