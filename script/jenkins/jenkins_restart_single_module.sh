# JDK的环境变量
export JAVA_HOME=/usr/local/jdk-11.0.14
export PATH=$JAVA_HOME/bin:$PATH

JAR_PATH=/opt/jenkins/package/spring-boot-001-hello-world/target
JAR_FILE=spring-boot-001-hello-world-0.0.1-SNAPSHOT.jar

ps -ef | grep $JAR_FILE | grep -v grep | awk '{print $2}' | xargs kill -9

#BUILD_ID=dontKillMe
java -jar $JAR_PATH/$JAR_FILE > $JAR_PATH/out.log 2>&1 &
if [ $? = 0 ];then
        echo  $JAR_FILE" restart success!!! process id:"`ps -ef | grep $JAR_FILE | grep -v grep | awk '{print $2}'`
fi