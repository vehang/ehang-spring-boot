#!/bin/sh
#最小构建的方式，需要检测lib包

# JDK的环境变量
export JAVA_HOME=/usr/local/jdk-11.0.14
export PATH=$JAVA_HOME/bin:$PATH

# 基础路径，由参数传入
# 多模块的时候，需要在路径中使用*统配一下多模块
# 比如/opt/ehang-spring-boot是多模块，下面由module1和module2
# 那么执行shell的时候使用：sh restart.sh /opt/ehang-spring-boot/\*  注意这里的*需要转义一下
JAR_BATH=$1
echo "Server校验 基础路径:"$JAR_BATH
JAR_PATH=${JAR_BATH}/tmp/*.jar

# 直接通过jar校验
jar_check_md5() {
  # jar 包的路径
  JAR_FILE=$1
  if [ ! -f $JAR_FILE ]; then
    # 如果校验的jar不存在 返回失败
    return 1
  fi

  JAR_MD5_FILE=${JAR_FILE}.md5
  if [ -f $JAR_MD5_FILE ]; then
    md5sum --status -c $JAR_MD5_FILE
    md5sum $JAR_FILE > $JAR_MD5_FILE
    return $?
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
  echo "Server校验 JAR包名称："$UNZIP_JAR_FILE_NAME
  # jar所在的路径
  UNZIP_JAR_FILE_BASE_PATH=${UNZIP_JAR_FILE%/${UNZIP_JAR_FILE_NAME}*}
  echo "Server校验 JAR包路径："$UNZIP_JAR_FILE_BASE_PATH
  # 解压的临时目录
  JAR_FILE_UNZIP_PATH=${UNZIP_JAR_FILE_BASE_PATH}/jar_unzip_tmp
  echo "Server校验 解压路径："$JAR_FILE_UNZIP_PATH

  # 用于缓存解压后文件详情的目录
  UNZIP_JAR_FILE_LIST=${UNZIP_JAR_FILE_BASE_PATH}/${UNZIP_JAR_FILE_NAME}.files
  # 缓存解压后文件详情的MD5
  UNZIP_JAR_FILE_LIST_MD5=${UNZIP_JAR_FILE_BASE_PATH}/${UNZIP_JAR_FILE_NAME}.files.md5

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

  md5sum --status -c $UNZIP_JAR_FILE_LIST_MD5
  md5sum $UNZIP_JAR_FILE_LIST > $UNZIP_JAR_FILE_LIST_MD5
  # 返回校验结果
  return $?
}

chenk_md5() {
  # jar 包的路径
  JAR_FILE=$1
  if [ -f $JAR_FILE ]; then
    # 直接通过jar校验
    jar_check_md5 $JAR_FILE
    if [ $? = 0 ];then
      echo "jenkins校验 通过Jar的MD5校验成功"
      return 0
    else
      echo "jenkins校验 通过Jar的MD5校验失败"
    fi

    # 通过解压jar 校验是否更新
    jar_unzip_check_md5 $JAR_FILE
    if [ $? = 0 ];then
      echo "jenkins校验 通过解压的MD5校验成功"
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
  echo "Server校验 JAR路径:"$JAR_FILE

  # 获取模块的基础路径，也就是target之前的路径
  MODULE_PATH=${JAR_FILE%/tmp/*}
  # jar包的名称
  JAR_NAME=`basename -s .jar $JAR_FILE`
  # 得到模块的名称
  MODULE_NAME=`basename $MODULE_PATH`

  # 在模块下创建一个临时的目录
  MODULE_TMP_PATH=${MODULE_PATH}/tmp

  # 在模块下临时目录下创建一个
  MODULE_TMP_LIB_PATH=${MODULE_TMP_PATH}/lib

  UPDATE=false
  # lib目录的路径
  MODULE_LIB_PATH=${MODULE_PATH}/target/lib
  if [ -d $MODULE_TMP_LIB_PATH ]; then
    # 将打包后的lib下的依赖全部拷贝到临时的lib文件夹下
    for LIB_JAR_FILE in ${MODULE_TMP_LIB_PATH}/*.jar
    do
      if [ -f $LIB_JAR_FILE ];then
        echo "Server校验 校验依赖Jar："$LIB_JAR_FILE
        chenk_md5 $LIB_JAR_FILE
        if [ $? = 0 ];then
          echo "Server校验 依赖lib校验！成功，没有发生变化："$LIB_JAR_FILE
        else
          echo "Server校验 依赖lib校验！失败，需要重新启动："$LIB_JAR_FILE
          UPDATE=true
          # 这里不结束的原因是为了计算所有模块的MD5值 以方便后续的校验
          # break
        fi
      fi
    done
  else
    echo "Server校验 模块没有lib目录..."
  fi

  MODULE_JAR=${MODULE_TMP_PATH}/${JAR_NAME}.jar
  echo "jenkins校验项目Jar："$MODULE_JAR
  chenk_md5 $JAR_FILE
  if [ $? = 0 ];then
     echo "Server校验 校验成功，没有发生变化"
  else
     echo "Server校验 校验失败，发现有jar包已经更新了，需要重新启动"
     UPDATE=true
  fi

  # 获取进程号 判断当前服务是否启动；如果Jar没变，但是服务未启动，也需要执行启动脚本
  PROCESS_ID=`ps -ef | grep $JAR_FILE | grep -v grep | awk '{print $2}'`
  # 如果不需要重启，但是进程号没有，说明当前jar没有启动，同样也需要启动一下
  if [ $UPDATE == false ] && [ ${#PROCESS_ID} == 0 ] ;then
     echo "Server校验 没有发现进程，说明服务未启动,需要启动服务"
     UPDATE=true
  fi

  # 如果是需要启动
  if [ $UPDATE == true ]; then
      # kill掉原有的进程
      ps -ef | grep $JAR_FILE | grep -v grep | awk '{print $2}' | xargs kill -9

      #如果出现Jenins Job执行完之后，进程被jenkins杀死，可尝试放开此配置项
      #BUILD_ID=dontKillMe
      #启动Jar
      if [ -d $MODULE_LIB_PATH ]; then
        echo "Server校验 loader.path指定lib的方式启动..."
        nohup java -Dloader.path=${MODULE_TMP_LIB_PATH} -jar $JAR_FILE > ${JAR_FILE}.log 2>&1 &
      else
        echo "Server校验 普通的方式启动..."
        nohup java -jar $JAR_FILE > ${JAR_FILE}.log 2>&1 &
      fi
      # =0 启动成功 =1 启动失败
      if [ $? == 0 ];then
          echo "Server校验 restart success!!! process id:" `ps -ef | grep $JAR_FILE | grep -v grep | awk '{print $2}'`
      else
          echo "Server校验 启动失败！"
      fi

      # 将最新的MD5值写入到缓存文件
      # md5sum $JAR_FILES_INFO > $JAR_FILES_INFO_MD5
  fi
  echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
  echo ""
fi
done