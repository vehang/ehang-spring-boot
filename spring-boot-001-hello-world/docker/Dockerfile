FROM openjdk:8

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 将target目录下的jar拷贝到容器
ADD ../target/*.jar /app.jar

EXPOSE 8081

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom" \
,"-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap" \
,"-jar", "/app.jar" ]




