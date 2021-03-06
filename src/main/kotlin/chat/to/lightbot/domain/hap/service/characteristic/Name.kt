package chat.to.lightbot.domain.hap.service.characteristic

// page 157
data class Name(override val instanceId: Int,
                override val accessoryInstanceId: Int,
                private val eventPublisher: (accessoryInstanceId: Int, characteristicInstanceId: Int, value: String) -> kotlin.Unit) : Characteristic {

    override val uuid = "00000023-0000-1000-8000-0026BB765291"
    override var value: String? = null
    override val type = "public.hap.characteristic.temperature.name"
    override val description: String? = null
    override val permissions = listOf(Permission.PAIRED_READ)
    override val format = Format.STRING
    override val unit: Unit? = null
    override val minimumValue: Double? = null
    override val maximumValue: Double? = null
    override val stepValue: Double? = null
    override val maximumLength: Int = 64
    override val maxDataLength: Int? = null

    fun updateName(name: String) {
        if (name.length > maximumLength) {
            throw IllegalArgumentException("Name value $name length must be lower than $maximumLength.")
        }

        if (value != name) {
            eventPublisher(accessoryInstanceId, instanceId, name)
        }

        value = name
    }
}