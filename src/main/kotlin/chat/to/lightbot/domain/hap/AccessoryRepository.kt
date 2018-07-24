package chat.to.lightbot.domain.hap

interface AccessoryRepository {

    fun store(accessory: Accessory)
    fun findAll(): List<Accessory>
    fun findByInstanceId(aid: Int): Accessory?

    // TODO only used in unit test. Find a better way
    fun clear()
}