FROM java:8
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
RUN bash -c "touch /app.jar"
RUN echo $(date) > /image_built_at
EXPOSE 8099
EXPOSE 8899
EXPOSE 9988
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8888","app.jar"]