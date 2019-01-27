# to.chat virtual home automation bot (vHab)

This simple demonstration shows how home automation can be implemented with Kotlin.

A simplified version of the Apple HomeKit Accessory Protocol Specification is implemented.

## Requirements

* Java 11
* Kotlin 1.3.20 (comes as maven dependency)
* Maven >= 3.2.1
* Running WeMaLa server

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

You will need a docker hub account and need access to https://hub.docker.com/r/larmic/wemala-server/.

```ssh
docker login --username=maryatdocker --email=mary@docker.com
docker pull larmic/wemala-vhab
docker run -p 8085:8085 -t larmic/wemala-vhab
```

and open ```http://localhost:8085```

## Demo requests

For update diner window send ``{ "characteristics" : [ { "aid" : 22000, "iid" : 22101, "value" : 100 } ]}``
to vhab bot ``vhab@to.chat``.

For update diner heater send ``{ "characteristics" : [ { "aid" : 21000, "iid" : 21101, "value" : 19.8 } ]}``
to vhab bot ``vhab@to.chat``.

For update diner lamp 1 send ``{ "characteristics" : [ { "aid" : 23000, "iid" : 23101, "value" : "on" } ]}``
to vhab bot ``vhab@to.chat``.