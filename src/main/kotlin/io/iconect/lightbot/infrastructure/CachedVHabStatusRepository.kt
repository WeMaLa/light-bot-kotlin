package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.VHabStatus
import io.iconect.lightbot.domain.VHabStatusRepository
import org.springframework.stereotype.Repository

@Repository
class CachedVHabStatusRepository : VHabStatusRepository {

    private var vHabStatus: VHabStatus = VHabStatus.STARTING

    override fun getStatus(): VHabStatus {
        return vHabStatus
    }

    override fun updateStatus(status: VHabStatus) {
        vHabStatus = status
    }

    // TODO only used in unit test. Find a better way
    override fun clear() {
        vHabStatus = VHabStatus.STARTING
    }
}