package io.iconect.lightbot.infrastructure

import io.iconect.lightbot.domain.Accessory
import io.iconect.lightbot.domain.AccessoryRepository
import org.springframework.stereotype.Repository

@Repository
class CachedAccessoryRepository : AccessoryRepository {

    private val accessories = mutableListOf<Accessory>()

    override fun store(accessory: Accessory) {
        accessories.removeIf { r -> r.instanceId == accessory.instanceId }
        accessories.add(accessory)
    }

    override fun findAll(): List<Accessory> {
        return accessories
    }

    override fun clear() {
        accessories.clear()
    }
}