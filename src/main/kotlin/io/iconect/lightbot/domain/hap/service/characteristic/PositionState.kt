package io.iconect.lightbot.domain.hap.service.characteristic

// page 170
data class PositionState(override val instanceId: Int) : Characteristic {

    override val uuid = "00000072-0000-1000-8000-0026BB765291"
    override var value: String = "0"
        private set
    override val type = "public.hap.characteristic.position.state"
    override val description: String? = null
    override val permissions = listOf(Permission.PAIRED_READ, Permission.NOTIFY)
    override val format = Format.UINT8
    override val unit: Unit? = Unit.OTHER
    override val minimumValue: Double = 0.0
    override val maximumValue: Double = 2.0
    override val stepValue: Double = 1.0
    override val maximumLength: Int? = null
    override val maxDataLength: Int? = null

}