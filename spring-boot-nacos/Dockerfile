FROM java:8
# WORKDIR /home
COPY spring-boot-nacos-1.0.jar app.jar
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=local", "app.jar" ]
