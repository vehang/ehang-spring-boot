mkdir ./tmp

cp ./spring-boot-001-hello-world/docker/* ./tmp
cp ./spring-boot-001-hello-world/target/*.jar ./tmp
cd ./tmp

docker build -t registry.cn-guangzhou.aliyuncs.com/ehang_jenkins/ehang-sping-boot-hello-world:latest ./tmp/.
docker push registry.cn-guangzhou.aliyuncs.com/ehang_jenkins/ehang-sping-boot-hello-world:latest

cd ..
#rm -rf ./tmp