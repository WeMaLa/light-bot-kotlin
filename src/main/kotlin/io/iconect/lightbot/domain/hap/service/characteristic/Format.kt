package io.iconect.lightbot.domain.hap.service.characteristic

// page 67
enum class Format(val json: String, val description: String) {

    BOOL("bool", "Boolean value expressed as one of the following: true, false, 0 (false), and 1 (true)."),
    UINT8("uint8", "Unsigned 8-bit integer."),
    UINT16("uint16", "Unsigned 16-bit integer."),
    UINT32("uint32", "Unsigned 32-bit integer."),
    UINT64("uint64", "Unsigned 64-bit integer."),
    INT("int", "Signed 32-bit integer."),
    FLOAT("float", "Signed 64-bit floating point number."),
    STRING("string", "Sequence of zero or more Unicode..."),

}