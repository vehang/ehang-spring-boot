FROM lib-jenjins-mini-build:latest

# 同步时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 将当前目录下的jar拷贝到容器类
ADD ./*.jar /app.jar

# 监听端口
EXPOSE 8081

# 启动
ENTRYPOINT ["java","-Dloader.path=/lib","-Djava.security.egd=file:/dev/./urandom" \
,"-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap" \
,"-jar", "/app.jar" ]




