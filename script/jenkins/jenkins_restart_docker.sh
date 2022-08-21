#!/bin/sh

# 基础路径，由参数传入
# 多模块的时候，需要在路径中使用*统配一下多模块
# 比如/opt/ehang-spring-boot是多模块，下面由module1和module2
# 那么执行shell的时候使用：sh restart.sh /opt/ehang-spring-boot/\*  注意这里的*需要转义一下
BASE_PATH=$1
echo "基础路径:"$BASE_PATH
DOCKER_COMPOSE_FILES=${BASE_PATH}/docker/docker-compose.yaml

DOCKER_PULL_SHELL=${BASE_PATH}/docker/docker-image-pull.sh

# 获取最新的镜像
for PULL_SHELL in $DOCKER_PULL_SHELL
do
if [ -f $PULL_SHELL ]
then
  echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
  echo "更新最新的镜像:"$PULL_SHELL
  sh $PULL_SHELL
  echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
  echo ""
fi
done

# 启动镜像
for DOCKER_COMPOSE_FILE in $DOCKER_COMPOSE_FILES
do
if [ -f $DOCKER_COMPOSE_FILE ]
then
  echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
  echo "docker compose 配置路径:"$DOCKER_COMPOSE_FILE

  docker-compose -f $DOCKER_COMPOSE_FILE up -d

  echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
  echo ""
fi
done