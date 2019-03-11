package chat.to.lightbot.domain.hap

import chat.to.server.bot.message.event.BotStatus

interface VHabStatusRepository {

    fun getStatus(): BotStatus

    fun updateStatus(status: BotStatus)

    // TODO only used in unit test. Find a better way
    fun clear()
}