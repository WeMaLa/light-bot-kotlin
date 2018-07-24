package chat.to.lightbot.domain.hap

interface VHabStatusRepository {

    fun getStatus(): VHabStatus

    fun updateStatus(status: VHabStatus)

    // TODO only used in unit test. Find a better way
    fun clear()
}