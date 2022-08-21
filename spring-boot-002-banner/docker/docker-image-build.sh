# 考虑到多模块的情况 这里创建一个临时目录，来汇总配置
rm -rf ./tmp
mkdir ./tmp

# 将脚本 jar包拷贝的临时目录中
cp ./spring-boot-002-banner/docker/* ./tmp
cp ./spring-boot-002-banner/target/*.jar ./tmp
cd ./tmp

# 构建镜像
docker build -t registry.cn-guangzhou.aliyuncs.com/ehang_jenkins/ehang-spring-boot-banner:latest .
# 将镜像推送到埃利园
docker push registry.cn-guangzhou.aliyuncs.com/ehang_jenkins/ehang-spring-boot-banner:latest

# 删除临时文件
cd ..
rm -rf ./tmp