#!/bin/sh

BUILD_SHELL_PATH=./*/docker/docker-image-build.sh

# 获取所有的构建脚本 开始遍历
for BUILD_SHELL in $BUILD_SHELL_PATH
do
if [ -f $BUILD_SHELL ]
then
  echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
  echo "构建脚本:"$BUILD_SHELL
  sh $BUILD_SHELL
  echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
  echo ""
fi
done