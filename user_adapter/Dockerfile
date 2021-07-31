FROM openjdk:8
COPY --from=hengyunabc/arthas:latest /opt/arthas /opt/arthas
ARG JAR_FILE
ADD target/${JAR_FILE} user_adapter.jar
ENTRYPOINT ["java","-jar", "-Duser.timezone=Asia/Shanghai", "-Dproject.name=user", "user_adapter.jar"]