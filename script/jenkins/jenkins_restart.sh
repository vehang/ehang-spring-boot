#!/bin/sh

# JDK的环境变量
export JAVA_HOME=/usr/local/jdk-11.0.14
export PATH=$JAVA_HOME/bin:$PATH

# 基础路径，由参数传入
# 多模块的时候，需要在路径中使用*统配一下多模块
# 比如/opt/ehang-spring-boot是多模块，下面由module1和module2
# 那么执行shell的时候使用：sh restart.sh /opt/ehang-spring-boot/\*  注意这里的*需要转义一下
JAR_BATH=$1
echo "基础路径:"$JAR_BATH
JAR_PATH=${JAR_BATH}/target/*.jar

# jar_check_md5 通过jar的md5值直接检测
# jar_unzip_check_md5 通过对jar包解压 校验文件详情的MD5
# check_md5 汇总上面两个方法的校验

# 直接通过jar校验
jar_check_md5() {
  # jar 包的路径
  JAR_FILE=$1
  if [ ! -f $JAR_FILE ]; then
    # 如果校验的jar不存在 返回失败
    return 1
  fi

  JAR_MD5_FILE=${JAR_FILE}.md5
  echo "Jenkins Docker镜像构建校验 JAR的MD5文件："$JAR_MD5_FILE
  if [ -f $JAR_MD5_FILE ]; then
    md5sum --status -c $JAR_MD5_FILE
    RE=$?
    md5sum $JAR_FILE > $JAR_MD5_FILE
    return $RE
  else
    md5sum $JAR_FILE > $JAR_MD5_FILE
  fi

  return 1
}

# 将Jar解压之后校验
jar_unzip_check_md5() {
  # jar 包的路径
  UNZIP_JAR_FILE=$1
  if [ ! -f $UNZIP_JAR_FILE ]; then
    # 如果校验的jar不存在 返回失败
    return 1
  fi

  # jar的名称
  UNZIP_JAR_FILE_NAME=`basename -s .jar $UNZIP_JAR_FILE`
  echo "Jenkins Docker镜像构建校验 JAR包名称："$UNZIP_JAR_FILE_NAME
  # jar所在的路径
  UNZIP_JAR_FILE_BASE_PATH=${UNZIP_JAR_FILE%/${UNZIP_JAR_FILE_NAME}*}
  echo "Jenkins Docker镜像构建校验 JAR包路径："$UNZIP_JAR_FILE_BASE_PATH
  # 解压的临时目录
  JAR_FILE_UNZIP_PATH=${UNZIP_JAR_FILE_BASE_PATH}/jar_unzip_tmp
  echo "Jenkins Docker镜像构建校验 解压路径："$JAR_FILE_UNZIP_PATH

  # 用于缓存解压后文件详情的目录
  UNZIP_JAR_FILE_LIST=${UNZIP_JAR_FILE_BASE_PATH}/${UNZIP_JAR_FILE_NAME}.files
  echo "Jenkins Docker镜像构建校验 jar文件详情路径："$UNZIP_JAR_FILE_LIST
  # 缓存解压后文件详情的MD5
  UNZIP_JAR_FILE_LIST_MD5=${UNZIP_JAR_FILE_BASE_PATH}/${UNZIP_JAR_FILE_NAME}.files.md5
  echo "Jenkins Docker镜像构建校验 jar文件详情MD5校验路径："$UNZIP_JAR_FILE_LIST

  rm -rf $JAR_FILE_UNZIP_PATH
  mkdir -p $JAR_FILE_UNZIP_PATH
  # 解压文件到临时目录
  unzip $UNZIP_JAR_FILE -d $JAR_FILE_UNZIP_PATH
  # 遍历解压目录，计算每个文件的MD5值及路径 输出到详情列表文件中
  find $JAR_FILE_UNZIP_PATH -type f -print | xargs md5sum > $UNZIP_JAR_FILE_LIST
  rm -rf $JAR_FILE_UNZIP_PATH

  if [ ! -f $UNZIP_JAR_FILE_LIST_MD5 ]; then
    # 如果校验文件不存在 直接返回校验失败
    md5sum $UNZIP_JAR_FILE_LIST > $UNZIP_JAR_FILE_LIST_MD5
    return 1
  fi

  # 根据上一次生成的MD5校验
  md5sum --status -c $UNZIP_JAR_FILE_LIST_MD5
  RE=$?
  # 生成最新的文件列表的MD5
  md5sum $UNZIP_JAR_FILE_LIST > $UNZIP_JAR_FILE_LIST_MD5
  # 返回校验结果
  return $RE
}

check_md5() {
  # jar 包的路径
  JAR_FILE=$1
  if [ -f $JAR_FILE ]; then
    # 直接通过jar校验
    jar_check_md5 $JAR_FILE
    if [ $? = 0 ];then
      echo "Jenkins Docker镜像构建校验 通过Jar的MD5校验成功"
      return 0
    else
      echo "Jenkins Docker镜像构建校验 通过Jar的MD5校验失败"
    fi

    # 通过解压jar 校验是否更新
    jar_unzip_check_md5 $JAR_FILE
    if [ $? = 0 ];then
      echo "Jenkins Docker镜像构建校验 通过解压的MD5校验成功"
      return 0
    else
      echo "Jenkins Docker镜像构建校验 通过解压的MD5校验失败"
    fi
  fi
  return 1
}

# 获取所有的JAR 开始遍历
for JAR_FILE in $JAR_PATH
do
if [ -f $JAR_FILE ]
then
  echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
  echo "JAR路径:"$JAR_FILE

  APP_UPDATE=false
  check_md5 $JAR_FILE
  if [ $? = 0 ];then
    echo "Jenkins Docker镜像构建校验lib！成功，没有发生变化"$JAR_FILE
  else
    APP_UPDATE=true
    echo "Jenkins Docker镜像构建校验lib！失败，已经更新"$JAR_FILE
  fi

  # 获取进程号 判断当前服务是否启动；如果Jar没变，但是服务未启动，也需要执行启动脚本
  PROCESS_ID=`ps -ef | grep $JAR_FILE | grep -v grep | awk '{print $2}'`
  # 如果不需要重启，但是进程号没有，说明当前jar没有启动，同样也需要启动一下
  if [ $APP_UPDATE == false ] && [ ${#PROCESS_ID} == 0 ] ;then
     echo "没有发现进程，说明服务未启动,需要启动服务"
     APP_UPDATE=true
  fi

  # 如果是需要启动
  if [ $APP_UPDATE == true ]; then
      # kill掉原有的进程
      ps -ef | grep $JAR_FILE | grep -v grep | awk '{print $2}' | xargs kill -9

      #如果出现Jenins Job执行完之后，进程被jenkins杀死，可尝试放开此配置项
      #BUILD_ID=dontKillMe
      #启动Jar
      nohup java -jar $JAR_FILE > ${JAR_FILE}.log 2>&1 &
      # =0 启动成功 =1 启动失败
      if [ $? == 0 ];then
          echo "restart success!!! process id:" `ps -ef | grep $JAR_FILE | grep -v grep | awk '{print $2}'`
      else
          echo "启动失败！"
      fi

      # 将最新的MD5值写入到缓存文件
      md5sum $JAR_FILES_INFO > $JAR_FILES_INFO_MD5
  fi
  echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
  echo ""
fi
done