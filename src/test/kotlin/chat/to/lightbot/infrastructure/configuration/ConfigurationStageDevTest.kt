package chat.to.lightbot.infrastructure.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


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
        assertThat(configuration.server?.url).isEqualTo("http://dev.to.chat")
    }
}