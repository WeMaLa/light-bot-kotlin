package io.iconect.lightbot.domain.hap.service.characteristic

// page 162
data class TargetTemperature(override val instanceId: Int, override var value: String = "10.0F", override var description: String? = null) : Characteristic {

    val uuid = "00000035-0000-1000-8000-0026BB765291"
    override val type = "public.hap.characteristic.temperature.target"
    override val perms = listOf(Permission.PAIRED_READ, Permission.PAIRED_WRITE, Permission.NOTIFY)
    override val format = Format.FLOAT
    override val unit: Unit? = Unit.CELSIUS
    override val minimumValue: Float? = 10.0F
    override val maximumValue: Float? = 38.0F
    override val stepValue: Float? = null
    override val maxLength: Int? = null
    override val maxDataLength: Int? = null
}