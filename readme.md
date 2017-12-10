# iconect.io

This is the light-bot-kotlin application.

## Requirements

* Java 1.8
* Maven >= 3.2.1

## Getting started with maven

```ssh
git clone https://github.com/WeMaLa/light-bot-kotlin
mvn clean package
mvn spring-boot:run
```

and open ```http://localhost:8080/api/rooms```

For health check open ```http://localhost:8080/system/health/```
and for metrics open ```http://localhost:8080/system/metrics/```
and for elastic open ```http://localhost:9200/```

## Pull and start docker image

You will need a docker hub account and need access to https://hub.docker.com/r/larmic/iconect-server/.

```ssh
docker login --username=maryatdocker --email=mary@docker.com
docker pull larmic/iconect-light-bot-kotlin
docker run -p 8080:8080 -t larmic/iconect-light-bot-kotlin
```

and open ```http://localhost:8080```