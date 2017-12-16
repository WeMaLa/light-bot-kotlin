package io.iconect.lightbot.domain.service.characteristic

// page 67
interface Characteristic {

    val type: String
    val instanceId: Int
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