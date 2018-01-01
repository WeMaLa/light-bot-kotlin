package io.iconect.lightbot.domain

interface VHabStatusRepository {

    fun getStatus(): VHabStatus

    fun updateStatus(status: VHabStatus)

    // TODO only used in unit test. Find a better way
    fun clear()
}