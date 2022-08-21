mkdir ./tmp

cp ./ehang-sping-boot-hello-world/docker/* ./tmp
cp ./ehang-sping-boot-hello-world/target/*.jar ./tmp

docker build -t registry.cn-guangzhou.aliyuncs.com/ehang_jenkins/ehang-sping-boot-hello-world:latest ./tmp/.
docker push registry.cn-guangzhou.aliyuncs.com/ehang_jenkins/ehang-sping-boot-hello-world:latest

#rm -rf ./tmp