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
@ActiveProfiles("unittest")
class ConfigurationTest {

    @Autowired
    lateinit var configuration: Configuration

    @Test
    fun `check configuration initializing`() {
        assertThat(configuration.bot?.identifier).isEqualTo("unit@test.bot")
        assertThat(configuration.bot?.username).isEqualTo("unit-test-bot-username")
        assertThat(configuration.bot?.password).isEqualTo("unit-test-bot-password")
        assertThat(configuration.server?.url).isEqualTo("http://server.unit.test/")
    }
}