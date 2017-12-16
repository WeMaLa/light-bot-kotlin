package io.iconect.lightbot.domain.hap.service.characteristic

// page 67
interface Characteristic {

    val type: String
    val instanceId: Int
    var value: String
    val perms: List<Permission>
    val description: String?
    val format: Format
    val unit: Unit?
    val minimumValue: Float?
    val maximumValue: Float?
    val stepValue: Float?
    val maxLength: Int?
    val maxDataLength: Int?

}