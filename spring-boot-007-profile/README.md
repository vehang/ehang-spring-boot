![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409184554560.png)

大家好，我是一航；

程序员开发一个新的功能，必定会经过本地环境的调试、测试环境测试、预发布环境灰度，最后才会正式推上正式环境提供给所有的用户使用；

那几天就来说说，SpringBoot项目如何才能优雅的管理不同的环境配置，从而轻松实现环境的切换，实现一次打包多环境运行。

> 后续学习SpringCloud的时候，不同环境的 配置项就可以放在远端的配置中心，项目运行时通过配置中心获取当前环境的配置文件，加载并启动；这里暂时只讲解单模块时，如何通过多个不同的配置实现环境的动态切换；



## 多环境切换

### 默认配置项application.yml

基础配置（公共配置），用于配置多个环境下相同的配置项，比如`端口号`、`项目名`、`默认环境`

```yml
spring:
  profiles:
    active: dev #默认环境 如果不配置，默认项就是：dev
  application:
    name: spring-boot-007-profile # 项目名
server:
  port: 18088 # 端口
```

### 配置多环境配置文件

在`resources`文件夹下添加多环境的配置文件；扫描的路径为`classpath:/,classpath:/config/,file:./,file:./config/*/,file:./config/`，也就是说，配置文件可以直接放在`resources`目录下，也可以放在`resources/config`目录下；

配置文件格式：**application-{profile}.properties**

如：

- 本地环境：application-dev.yml
- 测试环境：application-test.yml
- 正式环境：application-prod.yml

![image-20220409163238173](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409163238173.png)



### 环境切换

环境切换有三种方式，按优先级排序分别是：`命令行方式`、`JVM参数`、`配置文件`

- 命令行方式

  ```shell
  --spring.profiles.active=prod
  ```

  - IDEA开发工具配置`命令行参数`和`JVM参数`的方式

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409165723900.png)

  - jar包运行时

    ```shell
    java -jar spring-boot-007-profile-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
    ```

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409170811498.png)

- JVM参数

  ```shell
  -Dspring.profiles.active=prod
  ```

  - IDEA开发工具配置见命令行方式配图

  - jar包运行时指定

    ```shell
    java -jar -Dspring.profiles.active=prod spring-boot-007-profile-0.0.1-SNAPSHOT.jar
    ```

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409170930850.png)

- 配置文件

  ```yml
  spring:
    profiles:
      active: prod #默认环境 如果不配置，默认项就是：dev
  ```
  
  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409171105590.png)

​		

  

## 多端口指定

当我们做微服务的时候，同一个环境下为了负载，可能会在不同机器上运行多个服务，来处理用户的请求；但是本地测试的时候希望能够启动多个相同的服务，区别也就只是仅仅端口不同而已；

### IDEA配置多个启动项

- 按下面的步骤添加新的启动项

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409173801978.png)

  并指定不同的端口

  - 命令行方式

    ```shell
    --server.port=11223
    #或
    --spring.application.json="{\"server\":{\"port\":11223}}"
    ```

  - JVM方式

    ```shell
    -Dserver.port=11223
    #或
    -Dspring.application.json="{\"server\":{\"port\":11223}}"
    ```

- 启动不同的启动项

  按下面的操作步骤，即可执行不同的启动项

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409174753654.png)

  停止：

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409175344949.png)

​    

### Jar执行配置不同的端口

- `server.port`参数

  ```shell
  java -jar spring-boot-007-profile-0.0.1-SNAPSHOT.jar --server.port=11223
  #或
  java -jar -Dserver.port=11223 spring-boot-007-profile-0.0.1-SNAPSHOT.jar
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409175910997.png)

- `spring.application.json`参数

  ```shell
  java -jar spring-boot-007-profile-0.0.1-SNAPSHOT.jar --spring.application.json="{\"server\":{\"port\":11223}}"
  #或
  java -jar -Dspring.application.json="{\"server\":{\"port\":11223}}" spring-boot-007-profile-0.0.1-SNAPSHOT.jar
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409183232677.png)

- 环境变量`SERVER_PORT`

  - Windows

    ```shell
    SET SERVER_PORT=11223
    java -jar spring-boot-007-profile-0.0.1-SNAPSHOT.jar
    ```

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409182357362.png)

  - Linux

    ```shell
    SERVER_PORT=11223 java -jar spring-boot-007-profile-0.0.1-SNAPSHOT.jar
    ```

- 环境变量`SPRING_APPLICATION_JSON`

  - Windows

    ```shell
    SET SPRING_APPLICATION_JSON={"server":{"port":11223}}
    java -jar spring-boot-007-profile-0.0.1-SNAPSHOT.jar
    ```

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220409182834924.png)

  - Linux

    ```shell
    SPRING_APPLICATION_JSON={"server":{"port":11223}} java -jar spring-boot-007-profile-0.0.1-SNAPSHOT.jar
    ```

    


到此，环境的切换，端口的动态切换就说完了，这里仅仅就端口，更多的配置和扩展，可以基于以上的操作进行衍生；



---

最近在整理SpringBoot的学习教程，如果你也在学习SpringBoot，可以[点击查看](https://github.com/vehang/ehang-spring-boot)，欢迎交流！欢迎`star`

> 本文示例教程：https://github.com/vehang/ehang-spring-boot/tree/main/spring-boot-007-profile
>
> SpringBoot教程：https://github.com/vehang/ehang-spring-boot

## 联系我

| 公众号                                                       | 微信                                                         | 技术交流群                                                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/gzh_px_2022-03-21+23_04_24.png) | ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/wechat_px_2022-03-21+23_05_51.jpeg) | ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/group_px_2022-03-21+23_07_35.jpeg) |
