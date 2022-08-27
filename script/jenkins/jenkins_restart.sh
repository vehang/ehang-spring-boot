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

# 临时的解压目录
JAR_UNZIP_PATH=/tmp/jar_unzip_tmp

# 获取所有的JAR 开始遍历
for JAR_FILE in $JAR_PATH
do
if [ -f $JAR_FILE ]
then
  echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
  echo "JAR路径:"$JAR_FILE
  #JAR_FILE_MD5=${JAR_FILE}.md5

  # 解压目录的文件列表详情及MD5
  JAR_FILES_INFO=${JAR_FILE}_files
  # 详情列表文件的MD5详细
  JAR_FILES_INFO_MD5=${JAR_FILES_INFO}.md5

  # 删除解压后的临时文件夹 避免之前的缓存导致解压失败
  rm -rf $JAR_UNZIP_PATH
  # 解压文件
  unzip $JAR_FILE -d $JAR_UNZIP_PATH
  # 遍历解压目录，计算每个文件的MD5值及路径 输出到详情列表文件中
  find $JAR_UNZIP_PATH -type f -print | xargs md5sum > $JAR_FILES_INFO
  # 上面的这条命令等价于下面这个for循环
  #for file in `find $JAR_UNZIP_PATH`
  #do
  #  if [ -f $file ];then
  #    echo $file
  #    `md5sum $file >> $JAR_FILES_INFO`
  #  fi
  #done


  # 用于标记是否需要重启的标识
  RESTART=false

  # 判断MD5文件是否存在，存在就校验MD5值
  if [ -f $JAR_FILES_INFO_MD5 ]; then
    # 校验MD5
    md5sum --status -c $JAR_FILES_INFO_MD5
    # = 0表示校验成功 =1 表示校验失败
    if [ $? = 1 ];then
      echo "MD5校验失败,安装包已经更新！"
      RESTART=true
    else
      echo "与前一次的MD5匹配成功，说明安装包没有更新！"
    fi
  else
    echo "没有MD5值，说明是第一次启动"
    RESTART=true
  fi

  # 获取进程号 判断当前服务是否启动；如果Jar没变，但是服务未启动，也需要执行启动脚本
  PROCESS_ID=`ps -ef | grep $JAR_FILE | grep -v grep | awk '{print $2}'`
  # 如果不需要重启，但是进程号没有，说明当前jar没有启动，同样也需要启动一下
  if [ $RESTART == false ] && [ ${#PROCESS_ID} == 0 ] ;then
     echo "没有发现进程，说明服务未启动,需要启动服务"
     RESTART=true
  fi

  # 如果是需要启动
  if [ $RESTART == true ]; then
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