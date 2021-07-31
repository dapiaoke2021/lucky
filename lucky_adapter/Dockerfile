FROM openjdk:8
COPY --from=hengyunabc/arthas:latest /opt/arthas /opt/arthas
ARG JAR_FILE
ADD target/${JAR_FILE} lucky_adapter.jar
ENTRYPOINT ["java","-jar", "-Duser.timezone=Asia/Shanghai", "-Dproject.name=lucky", "lucky_adapter.jar"]