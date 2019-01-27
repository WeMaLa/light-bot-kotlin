package chat.to.lightbot.infrastructure

import chat.to.lightbot.domain.hap.Accessory
import chat.to.lightbot.domain.hap.AccessoryFactory
import chat.to.lightbot.domain.hap.AccessoryRepository
import chat.to.lightbot.infrastructure.model.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unittest")
class AccessoryControllerTest {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    private lateinit var accessoryFactory: AccessoryFactory

    @Autowired
    private lateinit var accessoryRepository: AccessoryRepository

    @BeforeEach
    fun setUp() {
        accessoryRepository.clear()
    }

    @Test
    fun `find all accessories and map to dto`() {
        accessoryRepository.store(Accessory(1, emptyList()))

        val exchange = testRestTemplate.exchange("/api/accessories", HttpMethod.GET, HttpEntity.EMPTY, AccessoriesDto::class.java)

        val accessories = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(accessories.accessories)
                .extracting("aid", "services.size")
                .containsOnly(tuple(1, 0))
    }

    @Test
    fun `find all accessories and map to string`() {
        val accessory = accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy")
        accessoryRepository.store(accessory)

        val exchange = testRestTemplate.exchange("/api/accessories", HttpMethod.GET, HttpEntity.EMPTY, String::class.java)

        val accessories = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)

        assertThat(accessories).isEqualTo("{" +
                "\"accessories\":[" +
                "{\"aid\":1,\"services\":[" +
                "{\"iid\":1,\"type\":\"4A\",\"characteristics\":[" +
                "{\"iid\":2,\"type\":\"35\",\"value\":\"10.0\",\"format\":\"float\",\"perms\":[\"pr\",\"pw\",\"Notify\"]}," +
                "{\"iid\":3,\"type\":\"11\",\"value\":\"0.0\",\"format\":\"float\",\"perms\":[\"pr\",\"Notify\"]}," +
                "{\"iid\":4,\"type\":\"23\",\"value\":\"dummy\",\"format\":\"string\",\"perms\":[\"pr\"]}" +
                "]}" +
                "]}" +
                "]" +
                "}")
    }

    @Test
    fun `find a specific accessory and map to dto`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))
        accessoryRepository.store(Accessory(2, emptyList()))

        val exchange = testRestTemplate.exchange("/api/accessories/1", HttpMethod.GET, HttpEntity.EMPTY, AccessoryDto::class.java)

        val accessory = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(accessory.aid).isEqualTo(1)
        assertThat(accessory.services.size).isEqualTo(1)
        assertThat(accessory.services[0].characteristics.size).isEqualTo(3)
        assertThat(accessory.services[0].characteristics)
                .extracting("iid", "type")
                .containsOnly(
                        tuple(2, "35"),
                        tuple(3, "11"),
                        tuple(4, "23")
                )
    }

    @Test
    fun `find a specific accessory and map to string`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))
        accessoryRepository.store(Accessory(2, emptyList()))

        val exchange = testRestTemplate.exchange("/api/accessories/1", HttpMethod.GET, HttpEntity.EMPTY, String::class.java)

        val accessory = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(accessory).isEqualTo("{\"aid\":1,\"services\":[" +
                "{\"iid\":1,\"type\":\"4A\",\"characteristics\":[" +
                "{\"iid\":2,\"type\":\"35\",\"value\":\"10.0\",\"format\":\"float\",\"perms\":[\"pr\",\"pw\",\"Notify\"]}," +
                "{\"iid\":3,\"type\":\"11\",\"value\":\"0.0\",\"format\":\"float\",\"perms\":[\"pr\",\"Notify\"]}," +
                "{\"iid\":4,\"type\":\"23\",\"value\":\"dummy\",\"format\":\"string\",\"perms\":[\"pr\"]}" +
                "]}" +
                "]}")
    }

    @Test
    fun `find not existing accessory`() {
        val exchange = testRestTemplate.exchange("/api/accessories/1", HttpMethod.GET, HttpEntity.EMPTY, ErrorMessageDto::class.java)

        val error = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(error.message).isEqualTo("Accessory 1 not found.")
    }

    @Test
    fun `find all services of a specific accessory and map to string`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/1/services", HttpMethod.GET, HttpEntity.EMPTY, String::class.java)

        val accessory = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(accessory).isEqualTo("[" +
                "{\"iid\":1,\"type\":\"4A\",\"characteristics\":[" +
                "{\"iid\":2,\"type\":\"35\",\"value\":\"10.0\",\"format\":\"float\",\"perms\":[\"pr\",\"pw\",\"Notify\"]}," +
                "{\"iid\":3,\"type\":\"11\",\"value\":\"0.0\",\"format\":\"float\",\"perms\":[\"pr\",\"Notify\"]}," +
                "{\"iid\":4,\"type\":\"23\",\"value\":\"dummy\",\"format\":\"string\",\"perms\":[\"pr\"]}" +
                "]}]")
    }

    @Test
    fun `find all services of a not existing accessory`() {
        val exchange = testRestTemplate.exchange("/api/accessories/1/services", HttpMethod.GET, HttpEntity.EMPTY, ErrorMessageDto::class.java)

        val errorMessageDto = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(errorMessageDto.message).isEqualTo("Accessory 1 not found.")
    }

    @Test
    fun `find a specific service of a specific accessory and map to dto`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/1/services/1", HttpMethod.GET, HttpEntity.EMPTY, ServiceDto::class.java)

        val service = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(service.iid).isEqualTo(1)
        assertThat(service.type).isEqualTo("4A")
        assertThat(service.characteristics)
                .extracting("iid", "type")
                .containsOnly(
                        tuple(2, "35"),
                        tuple(3, "11"),
                        tuple(4, "23")
                )
    }

    @Test
    fun `find a specific service of a specific accessory and map to string`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/1/services/1", HttpMethod.GET, HttpEntity.EMPTY, String::class.java)

        val service = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(service).isEqualTo("{\"iid\":1,\"type\":\"4A\",\"characteristics\":[" +
                "{\"iid\":2,\"type\":\"35\",\"value\":\"10.0\",\"format\":\"float\",\"perms\":[\"pr\",\"pw\",\"Notify\"]}," +
                "{\"iid\":3,\"type\":\"11\",\"value\":\"0.0\",\"format\":\"float\",\"perms\":[\"pr\",\"Notify\"]}," +
                "{\"iid\":4,\"type\":\"23\",\"value\":\"dummy\",\"format\":\"string\",\"perms\":[\"pr\"]}" +
                "]}")
    }

    @Test
    fun `find a not existing service of a specific accessory`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/1/services/15", HttpMethod.GET, HttpEntity.EMPTY, ErrorMessageDto::class.java)

        val error = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(error.message).isEqualTo("Service 15 of accessory 1 not found.")
    }

    @Test
    fun `find a specific service of a not existing accessory`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/15/services/1", HttpMethod.GET, HttpEntity.EMPTY, ErrorMessageDto::class.java)

        val error = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(error.message).isEqualTo("Accessory 15 not found.")
    }

    @Test
    fun `find all characteristics of a specific service of a specific accessory and map to string`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/1/services/1/characteristics", HttpMethod.GET, HttpEntity.EMPTY, String::class.java)

        val service = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(service).isEqualTo("[" +
                "{\"iid\":2,\"type\":\"35\",\"value\":\"10.0\",\"format\":\"float\",\"perms\":[\"pr\",\"pw\",\"Notify\"]}," +
                "{\"iid\":3,\"type\":\"11\",\"value\":\"0.0\",\"format\":\"float\",\"perms\":[\"pr\",\"Notify\"]}," +
                "{\"iid\":4,\"type\":\"23\",\"value\":\"dummy\",\"format\":\"string\",\"perms\":[\"pr\"]}" +
                "]")
    }

    @Test
    fun `find all characteristics of a not existing service of a specific accessory`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/1/services/15/characteristics", HttpMethod.GET, HttpEntity.EMPTY, ErrorMessageDto::class.java)

        val error = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(error.message).isEqualTo("Service 15 of accessory 1 not found.")
    }

    @Test
    fun `find all characteristics of a specific service of a not existing accessory`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/15/services/1/characteristics", HttpMethod.GET, HttpEntity.EMPTY, ErrorMessageDto::class.java)

        val error = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(error.message).isEqualTo("Accessory 15 not found.")
    }

    @Test
    fun `find specific characteristic of a specific service of a specific accessory and map to string`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/1/services/1/characteristics/3", HttpMethod.GET, HttpEntity.EMPTY, String::class.java)

        val characteristic = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(characteristic).isEqualTo("{\"iid\":3,\"type\":\"11\",\"value\":\"0.0\",\"format\":\"float\",\"perms\":[\"pr\",\"Notify\"]}")
    }

    @Test
    fun `find specific characteristic of a specific service of a specific accessory and map to dto`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/1/services/1/characteristics/4", HttpMethod.GET, HttpEntity.EMPTY, CharacteristicDto::class.java)

        val characteristic = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(characteristic.iid).isEqualTo(4)
        assertThat(characteristic.type).isEqualTo("23")
        assertThat(characteristic.value).isEqualTo("dummy")
        assertThat(characteristic.format).isEqualTo("string")
        assertThat(characteristic.perms).containsOnly("pr")
    }

    @Test
    fun `find specific characteristic of a not existing service of a specific accessory`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/1/services/15/characteristics/2", HttpMethod.GET, HttpEntity.EMPTY, ErrorMessageDto::class.java)

        val error = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(error.message).isEqualTo("Service 15 of accessory 1 not found.")
    }

    @Test
    fun `find specific characteristic of a specific service of a not existing accessory`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/15/services/1/characteristics/2", HttpMethod.GET, HttpEntity.EMPTY, ErrorMessageDto::class.java)

        val error = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(error.message).isEqualTo("Accessory 15 not found.")
    }

    @Test
    fun `find not existing characteristic of a specific service of a specific accessory`() {
        accessoryRepository.store(accessoryFactory.createThermostatAccessory(1, 1, 2, 3, 4, "dummy"))

        val exchange = testRestTemplate.exchange("/api/accessories/1/services/1/characteristics/22", HttpMethod.GET, HttpEntity.EMPTY, ErrorMessageDto::class.java)

        val error = exchange.body!!

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(error.message).isEqualTo("Characteristic 22 of service 1 of accessory 1 not found.")
    }

}