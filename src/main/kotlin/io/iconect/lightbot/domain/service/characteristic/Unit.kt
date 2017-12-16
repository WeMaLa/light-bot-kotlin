package io.iconect.lightbot.domain.service.characteristic

enum class Unit(val json: String, val description: String) {

    CELSIUS("celsius", "The unit is only \"degrees Celsius\"."),
    PERCENTAGE("percentage", "The unit is in percentage \"%\"."),
    ARC_DEGREES("arcdegrees", "The unit is in arc degrees."),
    LUX("lux", "The unit is in lux."),
    SECONDS("seconds", "The unit is in seconds.")

}