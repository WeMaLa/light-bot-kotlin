# iconect.io virtual home automation bot (vHab)

This simple demonstration shows how home automation can be implemented with Kotlin.

A simplified version of the Apple HomeKit Accessory Protocol Specification is implemented.

## Requirements

* Java 1.8
* Kotlin 1.2.10 (wird über Maven automatisch beim Build gezogen)
* Maven >= 3.2.1

## Getting started with maven

```ssh
git clone https://github.com/WeMaLa/light-bot-kotlin
mvn clean package
mvn spring-boot:run
```

and open ```http://localhost:8085/swagger-ui.html```

For health check open ```http://localhost:8080/actuator/health/```
and for metrics open ```http://localhost:8080/actuator/metrics/```
and for elastic open ```http://localhost:9200/```

## Pull and start docker image

You will need a docker hub account and need access to https://hub.docker.com/r/larmic/iconect-server/.

```ssh
docker login --username=maryatdocker --email=mary@docker.com
docker pull larmic/iconect-light-bot-kotlin
docker run -p 8085:8085 -t larmic/iconect-light-bot-kotlin
```

and open ```http://localhost:8085```

## Demo requests

For update diner window send ``{ "characteristics" : [ { "aid" : 22000, "iid" : 22101, "value" : 100 } ]}``
to iconect bot ``vhab@iconect.io``.

For update diner heater send ``{ "characteristics" : [ { "aid" : 21000, "iid" : 21101, "value" : 19.8 } ]}``
to iconect bot ``vhab@iconect.io``.

For update diner lamp 1 send ``{ "characteristics" : [ { "aid" : 23000, "iid" : 23101, "value" : "on" } ]}``
to iconect bot ``vhab@iconect.io``.