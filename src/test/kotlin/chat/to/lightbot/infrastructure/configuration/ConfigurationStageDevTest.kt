package chat.to.lightbot.infrastructure.configuration

import chat.to.lightbot.infrastructure.configuration.Configuration
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("dev")
class ConfigurationStageDevTest {

    @Autowired
    lateinit var configuration: Configuration

    @Test
    fun `check configuration initializing on default stage`() {
        Assertions.assertThat(configuration.bot?.identifier).isEqualTo("vhab@iconect.io")
        Assertions.assertThat(configuration.bot?.username).isEqualTo("vhab")
        Assertions.assertThat(configuration.bot?.password).isEqualTo("vhab-1234")
        Assertions.assertThat(configuration.bot?.notificationUrl).isEqualTo("http://vhab.iconect.larmic.de/api/notify")
        Assertions.assertThat(configuration.server?.url).isEqualTo("http://dev.server.iconect.larmic.de")
    }
}