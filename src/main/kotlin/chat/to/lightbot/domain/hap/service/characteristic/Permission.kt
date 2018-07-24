package chat.to.lightbot.domain.hap.service.characteristic

enum class Permission(val json: String) {

    PAIRED_READ("pr"),
    PAIRED_WRITE("pw"),
    NOTIFY("Notify")

}