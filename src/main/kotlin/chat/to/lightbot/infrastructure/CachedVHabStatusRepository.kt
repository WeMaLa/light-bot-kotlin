package chat.to.lightbot.infrastructure

import chat.to.lightbot.domain.hap.VHabStatus
import chat.to.lightbot.domain.hap.VHabStatusRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Repository

@Repository
class CachedVHabStatusRepository @Autowired constructor(private val messagingTemplate: SimpMessagingTemplate) : VHabStatusRepository {

    private var vHabStatus: VHabStatus = VHabStatus.STARTING

    override fun getStatus(): VHabStatus {
        return vHabStatus
    }

    @EventListener
    override fun updateStatus(status: VHabStatus) {
        if (status != vHabStatus) {
            messagingTemplate.convertAndSend("/topic/status", status.name)
        }
        vHabStatus = status
    }

    // TODO only used in unit test. Find a better way
    override fun clear() {
        vHabStatus = VHabStatus.STARTING
    }
}