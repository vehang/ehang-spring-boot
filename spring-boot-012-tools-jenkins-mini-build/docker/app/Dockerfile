# 集成自依赖基础镜像
FROM lib-jenjins-mini-build:latest

# 将当前目录下的jar拷贝到容器类
ADD ./*.jar /app.jar

# 监听端口
EXPOSE 18092

# 启动
ENTRYPOINT ["java","-Dloader.path=/lib","-Djava.security.egd=file:/dev/./urandom" \
,"-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap" \
,"-jar", "/app.jar" ]




