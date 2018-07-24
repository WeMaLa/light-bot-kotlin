package chat.to.lightbot.infrastructure.configuration

import org.assertj.core.api.Assertions.assertThat
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
        assertThat(configuration.bot?.identifier).isEqualTo("vhab@to.chat")
        assertThat(configuration.bot?.username).isEqualTo("vhab")
        assertThat(configuration.bot?.password).isEqualTo("vhab-1234")
        assertThat(configuration.bot?.notificationUrl).isEqualTo("http://vhab.larmic.de/api/notify")
        assertThat(configuration.server?.url).isEqualTo("http://dev.larmic.de")
    }
}