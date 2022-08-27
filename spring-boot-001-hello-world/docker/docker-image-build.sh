# 考虑到多模块的情况 这里创建一个临时目录，来汇总配置
# 项目目录
MODULE_BATH_PATH=./spring-boot-001-hello-world
mkdir -p "${MODULE_TMP_PATH}"

# 项目的临时文件目录
MODULE_TMP_PATH=${MODULE_BATH_PATH}/tmp
mkdir -p "${MODULE_TMP_PATH}"

# 项目的lib目录 用于保存项目所有赖
MODULE_LIB_TMP_PATH=${MODULE_TMP_PATH}/lib
mkdir -p "${MODULE_LIB_TMP_PATH}"

# jar解压之后的目录
MODULE_UNZIP_TMP_PATH=${MODULE_TMP_PATH}/jar_unzip_tmp
# 用于保存解压有的jar包中文件的详情及MD5值
JAR_FILES_INFO=${MODULE_TMP_PATH}/jar_files
# 详情文件的MD5信息
JAR_FILES_INFO_MD5=${JAR_FILES_INFO}.md5

# 将脚本 jar包拷贝的临时目录中 强制复制
\cp -r ${MODULE_BATH_PATH}/docker/* ${MODULE_TMP_PATH}
# 拷贝jar
\cp -r ${MODULE_BATH_PATH}/target/*.jar ${MODULE_TMP_PATH}
# 拷贝lib
\cp -r ${MODULE_BATH_PATH}/lib/* ${MODULE_LIB_TMP_PATH}
# 进入目录tmp目录
cd ${MODULE_BATH_PATH}

# 讲jar解压到指定的解压目录
unzip ${MODULE_TMP_PATH}/*.jar -d ${MODULE_UNZIP_TMP_PATH}
# 查找并输出所有的
find $JAR_UNZIP_PATH -type f -print | xargs md5sum > $JAR_FILES_INFO


UPDATE=false
if [ -f $JAR_FILES_INFO_MD5 ]; then
  # 校验MD5
  md5sum --status -c $JAR_FILES_INFO_MD5
  # = 0表示校验成功 =1 表示校验失败
  if [ $? = 1 ];then
    echo "MD5校验失败,安装包已经更新！需要构建镜像"
    UPDATE=true
  else
    echo "与前一次的MD5匹配成功，说明安装包没有更新，无需重新构建！"
  fi
else
  echo "没有MD5值，说明是第一次构建"
  UPDATE=true
fi

if [ $UPDATE == true ]; then
  # 构建镜像
  docker build -t registry.cn-guangzhou.aliyuncs.com/ehang_jenkins/ehang-sping-boot-hello-world:latest .
  # 将镜像推送到埃利园
  docker push registry.cn-guangzhou.aliyuncs.com/ehang_jenkins/ehang-sping-boot-hello-world:latest

  # 将最新的MD5值写入到缓存文件
  md5sum $JAR_FILES_INFO > $JAR_FILES_INFO_MD5
fi
# 删除临时文件
# cd ..
# rm -rf ./tmp