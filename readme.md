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

and open ```http://localhost:8085/api/rooms```

For health check open ```http://localhost:8080/system/health/```
and for metrics open ```http://localhost:8080/system/metrics/```
and for elastic open ```http://localhost:9200/```

## Pull and start docker image

You will need a docker hub account and need access to https://hub.docker.com/r/larmic/iconect-server/.

```ssh
docker login --username=maryatdocker --email=mary@docker.com
docker pull larmic/iconect-light-bot-kotlin
docker run -p 8085:8085 -t larmic/iconect-light-bot-kotlin
```

and open ```http://localhost:8085```

## Demo request

send ``{ "characteristics" : [ { "aid" : 1, "iid" : 21, "value" : 23.3 } ]}``
to iconect bot ``light-bot@iconect.io``