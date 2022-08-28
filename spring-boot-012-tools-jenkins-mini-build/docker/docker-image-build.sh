# 该脚本是用于拆分业务jar和lib的检测，打包，部署

# 基础的lib镜像
MODULE_DOCKER_LIB_IMAGE_NAME=lib-jenjins-mini-build
# app的镜像名称
MODULE_DOCKER_IMAGE_NAME=ehang-sping-boot-jenkins-mini-build
# 基础路径
MODULE_BATH_PATH=./spring-boot-012-tools-jenkins-mini-build

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

MODULE_LIB_PATH=${MODULE_BATH_PATH}/docker/lib
MODULE_APP_PATH=${MODULE_BATH_PATH}/docker/app

\cp -r ${MODULE_BATH_PATH}/target/*.jar ${MODULE_APP_PATH}
\cp -r ${MODULE_BATH_PATH}/target/lib/*.jar ${MODULE_LIB_PATH}

LIB_UPDATE=false
for LIB_JAR_FILE in ${MODULE_LIB_PATH}/*.jar
do
  echo $LIB_JAR_FILE
  if [ -f $LIB_JAR_FILE ];then
    echo "Jenkins Docker镜像构建校验lib 依赖Jar："$LIB_JAR_FILE
    check_md5 $LIB_JAR_FILE
    if [ $? = 0 ];then
      echo "Jenkins Docker镜像构建校验lib！成功，没有发生变化"$LIB_JAR_FILE
    else
      LIB_UPDATE=true
      echo "Jenkins Docker镜像构建校验lib！失败，已经更新"$LIB_JAR_FILE
    fi
  fi
done
# 一旦发现lib有变化，就构建新的lib镜像
if [ $LIB_UPDATE = true ]; then
  docker build -t ${MODULE_DOCKER_LIB_IMAGE_NAME}:latest ${MODULE_LIB_PATH}/.
fi

APP_UPDATE=false
for APP_JAR_FILE in ${MODULE_APP_PATH}/*.jar
do
  echo $APP_JAR_FILE
  if [ -f $APP_JAR_FILE ];then
    echo "Jenkins Docker镜像构建校验APP 依赖Jar："$APP_JAR_FILE
    check_md5 $APP_JAR_FILE
    if [ $? = 0 ];then
      echo "Jenkins Docker镜像构建校验APP！成功，没有发生变化"$APP_JAR_FILE
    else
      APP_UPDATE=true
      echo "Jenkins Docker镜像构建校验APP！失败，已经更新"$APP_JAR_FILE
    fi
  fi
done
# 一旦发现lib有变化，或者APP发生变化 都需要构建新的镜像
if [ $APP_UPDATE = true ] || [ $LIB_UPDATE = true ]; then
  # 构建镜像
  docker build -t registry.cn-guangzhou.aliyuncs.com/ehang_jenkins/${MODULE_DOCKER_IMAGE_NAME}:latest ${MODULE_APP_PATH}/.
  # 将镜像推送到阿里云
  docker push registry.cn-guangzhou.aliyuncs.com/ehang_jenkins/${MODULE_DOCKER_IMAGE_NAME}:latest
fi