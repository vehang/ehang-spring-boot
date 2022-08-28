#!/bin/sh
# Jenkins 构建完之后 检测jar包 依赖jar是否有更新
# 只把有更新的jar推送到远端服务器
# 最小构建的方式，需要检测lib包

# JDK的环境变量
export JAVA_HOME=/usr/local/jdk-11.0.14
export PATH=$JAVA_HOME/bin:$PATH

# 基础路径，由参数传入
# 多模块的时候，需要在路径中使用*统配一下多模块
# 比如/opt/ehang-spring-boot是多模块，下面由module1和module2
# 那么执行shell的时候使用：sh restart.sh /opt/ehang-spring-boot/\*  注意这里的*需要转义一下
JAR_BATH=$1
echo "jenkins校验 基础路径:"$JAR_BATH
JAR_PATH=${JAR_BATH}/target/*.jar

# 临时的解压目录
JAR_UNZIP_PATH=/tmp/jar_unzip_tmp

# 直接通过jar校验
jar_check_md5() {
  # jar 包的路径
  JAR_FILE=$1
  if [ ! -f $JAR_FILE ]; then
    # 如果校验的jar不存在 返回失败
    return 1
  fi

  JAR_MD5_FILE=${JAR_FILE}.md5
  echo "jenkins校验 JAR的MD5文件："$JAR_MD5_FILE
  if [ -f $JAR_MD5_FILE ]; then
    cat $JAR_MD5_FILE
    md5sum $JAR_FILE
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
  echo "jenkins校验 JAR包名称："$UNZIP_JAR_FILE_NAME
  # jar所在的路径
  UNZIP_JAR_FILE_BASE_PATH=${UNZIP_JAR_FILE%/${UNZIP_JAR_FILE_NAME}*}
  echo "jenkins校验 JAR包路径："$UNZIP_JAR_FILE_BASE_PATH
  # 解压的临时目录
  JAR_FILE_UNZIP_PATH=${UNZIP_JAR_FILE_BASE_PATH}/jar_unzip_tmp
  echo "jenkins校验 解压路径："$JAR_FILE_UNZIP_PATH

  # 用于缓存解压后文件详情的目录
  UNZIP_JAR_FILE_LIST=${UNZIP_JAR_FILE_BASE_PATH}/${UNZIP_JAR_FILE_NAME}.files
  echo "jenkins校验 jar文件详情路径："$UNZIP_JAR_FILE_LIST
  # 缓存解压后文件详情的MD5
  UNZIP_JAR_FILE_LIST_MD5=${UNZIP_JAR_FILE_BASE_PATH}/${UNZIP_JAR_FILE_NAME}.files.md5
  echo "jenkins校验 jar文件详情MD5校验路径："$UNZIP_JAR_FILE_LIST

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

  cat $UNZIP_JAR_FILE_LIST_MD5
  md5sum $UNZIP_JAR_FILE_LIST
  md5sum --status -c $UNZIP_JAR_FILE_LIST_MD5
  RE=$?
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
      echo "jenkins校验 通过Jar的MD5校验成功"
      rm -f $JAR_FILE
      return 0
    else
      echo "jenkins校验 通过Jar的MD5校验失败"
    fi

    # 通过解压jar 校验是否更新
    jar_unzip_check_md5 $JAR_FILE
    if [ $? = 0 ];then
      echo "jenkins校验 通过解压的MD5校验成功"
      rm -f $JAR_FILE
      return 0
    else
      echo "jenkins校验 通过解压的MD5校验失败"
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
  echo "jenkins校验 业务JAR路径:"$JAR_FILE
  #JAR_FILE_MD5=${JAR_FILE}.md5

  # 获取模块的基础路径，也就是target之前的路径
  MODULE_PATH=${JAR_FILE%/target/*}
  # jar包的名称
  JAR_NAME=`basename -s .jar $JAR_FILE`
  # 得到模块的名称
  MODULE_NAME=`basename $MODULE_PATH`

  # 在模块下创建一个临时的目录
  MODULE_TMP_PATH=${MODULE_PATH}/tmp
  mkdir -p $MODULE_TMP_PATH

  # 在模块下临时目录下创建一个
  MODULE_TMP_LIB_PATH=${MODULE_TMP_PATH}/lib
  mkdir -p $MODULE_TMP_LIB_PATH

  # 将jar包拷贝到临时目录
  \cp -r $JAR_FILE $MODULE_TMP_PATH

  # lib目录的路径
  MODULE_LIB_PATH=${MODULE_PATH}/target/lib
  echo "jenkins校验 lib目录："$MODULE_LIB_PATH
  if [ -d $MODULE_LIB_PATH ]; then
    # 将打包后的lib下的依赖全部拷贝到临时的lib文件夹下
    \cp -r ${MODULE_LIB_PATH}/* ${MODULE_TMP_LIB_PATH}
    for LIB_JAR_FILE in ${MODULE_TMP_LIB_PATH}/*.jar
    do
      echo $LIB_JAR_FILE
      if [ -f $LIB_JAR_FILE ];then
        echo "jenkins校验依赖Jar："$LIB_JAR_FILE
        check_md5 $LIB_JAR_FILE
        if [ $? = 0 ];then
          echo "jenkins依赖lib校验！成功，没有发生变化"$LIB_JAR_FILE
        else
          echo "jenkins依赖lib校验！失败，已经更新"$LIB_JAR_FILE
        fi
      fi
    done
  fi

  MODULE_JAR=${MODULE_TMP_PATH}/${JAR_NAME}.jar
  echo "jenkins校验项目Jar："$MODULE_JAR
  check_md5 $MODULE_JAR
  if [ $? = 0 ];then
     echo "jenkins校验成功，没有发生变化"
  else
     echo "jenkins校验失败，已经更新"
  fi
fi
done