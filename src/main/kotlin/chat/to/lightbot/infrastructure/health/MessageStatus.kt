package chat.to.lightbot.infrastructure.health

import chat.to.lightbot.domain.hap.VHabStatusRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.stereotype.Component

@Component
class MessageStatus @Autowired constructor(private val vHabStatusRepository: VHabStatusRepository) : AbstractHealthIndicator() {

    override fun doHealthCheck(builder: Health.Builder) {
        builder.status(vHabStatusRepository.getStatus().name).build()
    }

}