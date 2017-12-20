package io.iconect.lightbot.infrastructure.message.model

// page 80
enum class HapStatusCode(val code: Int, val description: String) {

    C0(0, "This specifies a success for the request."),
    C70401(-70401, "Request denied due to insufficient privileges."),
    C70402(-70402, "Unable to communicate with requested service, e.g. the power to the accessory was turned off."),
    C70403(-70403, "Resource is busy, try again."),
    C70404(-70404, "Cannot write to read only characteristic."),
    C70405(-70405, "Cannot read from a write only characteristic."),
    C70406(-70406, "Notification is not supported for characteristic."),
    C70407(-70407, "Out of resources to process request."),
    C70408(-70408, "Operation timed out."),
    C70409(-70409, "Resource does not exist."),
    C70410(-70410, "Accessory received an invalid value in a write request."),
    C70411(-70411, "Insufficient Authorization.")

}