package io.iconect.lightbot.domain.hap.service.characteristic

// page 148
data class CurrentTemperature(override val instanceId: Int, override var value: String = "0.0F", override var description: String? = null) : Characteristic {

    val uuid = "00000011-0000-1000-8000-0026BB765291"
    override val type = "public.hap.characteristic.temperature.current"
    override val perms = listOf(Permission.PAIRED_READ, Permission.NOTIFY)
    override val format = Format.FLOAT
    override val unit: Unit? = Unit.CELSIUS
    override val minimumValue: Float? = 0.0F
    override val maximumValue: Float? = 100.0F
    override val stepValue: Float? = 0.1F
    override val maxLength: Int? = null
    override val maxDataLength: Int? = null

}