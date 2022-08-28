# 这里用于构建一个只包含依赖的基础镜像
# 如果依赖更新的，就重新构建
# 如果依赖没有更新，就只更新业务的jar
FROM openjdk:8

# 同步时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 将lib目录下的所有jar全部拷贝到镜像里面
ADD ./*.jar /lib/




