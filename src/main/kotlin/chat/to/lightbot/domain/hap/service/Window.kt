package chat.to.lightbot.domain.hap.service

import chat.to.lightbot.domain.hap.service.characteristic.CurrentPosition
import chat.to.lightbot.domain.hap.service.characteristic.Name
import chat.to.lightbot.domain.hap.service.characteristic.TargetPosition

// page 228
class Window(override val instanceId: Int,
             override val accessoryInstanceId: Int,
             private val targetPositionInstanceId: Int,
             private val currentPositionInstanceId: Int,
             private val nameInstanceId: Int,
             private val eventPublisher: (accessoryInstanceId: Int, characteristicInstanceId: Int, value: String) -> Unit) : Service {

    override val uuid = "0000008B-0000-1000-8000-0026BB765291"
    override val type = "public.hap.service.window"
    override val characteristics = listOf(
            TargetPosition(targetPositionInstanceId, accessoryInstanceId, eventPublisher),
            CurrentPosition(currentPositionInstanceId, accessoryInstanceId, eventPublisher),
            Name(nameInstanceId, accessoryInstanceId, eventPublisher))
    override val hidden = false
    override val primaryService = true
    override val linkedServices = emptyList<Int>()

}