# to.chat virtual home automation bot (vHab)

This simple demonstration shows how home automation can be docker to the server implemented with Kotlin.

A simplified version of the Apple HomeKit Accessory Protocol Specification is used.

## Requirements

* Java 11
* Kotlin 1.3.31 (comes as maven dependency)
* Maven >= 3.2.1
* Running WeMaLa server (or using cloud server dev.to.chat)

### Requirements for publishing an artifact

We use official docker hub to publish docker images and nexus on premise.

* Access to https://hub.docker.com/ (to publish a docker image)
* Access to http://nexus.to.chat/ (to publish a jar)

In maven settings.xml

```
<server>
   <id>docker.io</id>
   <username>my-docker-hub-username</username>
   <password>my-docker-hub-password</password>
   <configuration>
     <email>my-docker-hub-email</email>
   </configuration>
</server>
<server>
   <id>wemala</id>
   <username>my-nexus-username</username>
   <password>my-nexus-password</password>
</server>
<server>
   <id>wemala-snapshots</id>
   <username>my-nexus-username</username>
   <password>my-nexus-password</password>
</server>
```

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

You will need a docker hub account and need access to https://hub.docker.com/r/larmic/wemala-vhab/.

```ssh
docker login --username=maryatdocker --email=mary@docker.com
docker pull larmic/wemala-vhab
docker run --rm --name vhab -e "SPRING_PROFILES_ACTIVE=dev" -e JAVA_PROPERTIES="--wemala.bot.identifier=my-vhab@to.chat --wemala.bot.password=my-vhab-password" -p 8085:8085 -t larmic/wemala-vhab
```

and open ```http://localhost:8085```

## Demo requests

For update diner window send ``{ "characteristics" : [ { "aid" : 22000, "iid" : 22101, "value" : 100 } ]}``
to vhab bot ``vhab@to.chat``.

For update diner heater send ``{ "characteristics" : [ { "aid" : 21000, "iid" : 21101, "value" : 19.8 } ]}``
to vhab bot ``vhab@to.chat``.

For update diner lamp 1 send ``{ "characteristics" : [ { "aid" : 23000, "iid" : 23101, "value" : "on" } ]}``
to vhab bot ``vhab@to.chat``.