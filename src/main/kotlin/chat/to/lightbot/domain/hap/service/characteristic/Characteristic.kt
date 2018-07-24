package chat.to.lightbot.domain.hap.service.characteristic

// page 67
interface Characteristic {

    val uuid: String
    val type: String
    val instanceId: Int
    val accessoryInstanceId: Int
    val value: String?
    val permissions: List<Permission>
    val description: String?
    val format: Format
    val unit: Unit?
    val minimumValue: Double?
    val maximumValue: Double?
    val stepValue: Double?
    val maximumLength: Int?
    val maxDataLength: Int?

}

interface WritableCharacteristic : Characteristic {
    fun adjustValue(value: String)
}