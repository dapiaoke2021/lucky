# 基础镜像
FROM java:8

# 维护人
MAINTAINER liuyuchan286<liuyuchan286@gmail.com>

ENV TZ="Asia/Shanghai"
RUN date

# 将本地文件夹挂载到当前容器
VOLUME /tmp

# 拷贝文件到容器
ADD target/renren-fast.jar app.jar


RUN sh -c 'touch /app.jar'

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime

# 开放端口
EXPOSE 8088

# 配置容器启动后执行的命令
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -Xms512m -Xmx1024m -jar /app.jar " ]
