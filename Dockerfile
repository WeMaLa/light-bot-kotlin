# using open jdk
# https://github.com/docker-library/openjdk/tree/master/8-jdk

# with memory settings are using container limits
# https://developers.redhat.com/blog/2017/03/14/java-inside-docker/

FROM openjdk:11.0.1-jre-slim
VOLUME /tmp
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-XX:+PrintFlagsFinal -XX:+UnlockExperimentalVMOptions"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar ${JAVA_PROPERTIES}" ]