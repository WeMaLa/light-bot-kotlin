package chat.to.lightbot.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Component
@ConfigurationProperties("wemala")
@Validated
class Configuration {

    // TODO does not work with lateinit?
    @NotNull
    var bot: Bot? = null

    // TODO does not work with lateinit?
    @NotNull
    var server: Server? = null
}

class Server {

    @NotBlank
    lateinit var url: String

}

class Bot {

    @NotBlank
    lateinit var identifier: String

    @NotBlank
    lateinit var password: String

    @NotBlank
    lateinit var username: String

    @NotBlank
    lateinit var notificationUrl: String

}