package io.iconect.lightbot.domain.hap

interface AccessoryRepository {

    fun store(accessory: Accessory)
    fun findAll(): List<Accessory>

    // TODO only used in unit test. Find a better way
    fun clear()
}