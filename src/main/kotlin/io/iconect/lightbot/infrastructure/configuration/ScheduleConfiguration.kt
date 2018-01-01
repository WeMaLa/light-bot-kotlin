package io.iconect.lightbot.infrastructure.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@Profile("!unittest")
@EnableScheduling
class ScheduleConfiguration