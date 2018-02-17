package io.iconect.lightbot.infrastructure.configuration

import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class ConfigurationStageDefaultTest {

    @Autowired
    lateinit var configuration: Configuration

    @Test
    fun `check configuration initializing on default stage`() {
        Assertions.assertThat(configuration.bot?.identifier).isEqualTo("vhab-bot@iconect.io")
        Assertions.assertThat(configuration.bot?.username).isEqualTo("vhab")
        Assertions.assertThat(configuration.bot?.password).isEqualTo("vhab")
        Assertions.assertThat(configuration.bot?.notificationUrl).isEqualTo("http://localhost:8085/api/notify")
        Assertions.assertThat(configuration.server?.url).isEqualTo("http://localhost:8080")
    }
}