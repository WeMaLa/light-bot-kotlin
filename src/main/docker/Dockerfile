# using open jdk
# https://github.com/docker-library/openjdk/tree/master/8-jdk

# with memory settings are using container limits
# https://developers.redhat.com/blog/2017/03/14/java-inside-docker/

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD vhab-kotlin.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-XX:+PrintFlagsFinal -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar ${JAVA_PROPERTIES}" ]