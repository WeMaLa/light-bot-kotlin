package io.iconect.lightbot.domain.hap.service

import io.iconect.lightbot.domain.hap.service.characteristic.Characteristic

// page 228
class Window(override val instanceId: Int) : Service {

    override val uuid = "0000008B-0000-1000-8000-0026BB765291"
    override val type = "public.hap.service.window"
    override val characteristics = emptyList<Characteristic>()
    override val hidden = false
    override val primaryService = true
    override val linkedServices = emptyList<Int>()

}