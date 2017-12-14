package io.iconect.lightbot.infrastructure.configuration

import io.iconect.lightbot.TestLightBotApplication
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("dev")
@ContextConfiguration(classes = [TestLightBotApplication::class])
class ConfigurationStageDevTest {

    @Autowired
    lateinit var configuration: Configuration

    @Test
    fun `check configuration initializing on default stage`() {
        Assertions.assertThat(configuration.bot?.identifier).isEqualTo("light-bot@iconect.io")
        Assertions.assertThat(configuration.bot?.username).isEqualTo("light-bot")
        Assertions.assertThat(configuration.bot?.password).isEqualTo("light-bot")
        Assertions.assertThat(configuration.bot?.notificationUrl).isEqualTo("http://light-bot.iconect.larmic.de/api/notify")
        Assertions.assertThat(configuration.server?.url).isEqualTo("http://dev.server.iconect.larmic.de/api/")
    }
}