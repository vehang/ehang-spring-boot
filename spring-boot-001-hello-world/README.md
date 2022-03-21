![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313162054091.png)

大家好，我是一航！

今天带大家来创建我们的第一个SpringBoot项目，开启一段全新，完全不一样的旅程....

## 什么是SpringBoot

Spring Boot是由Pivotal团队提供的全新框架，其设计目的是用来简化新Spring应用的初始搭建以及开发过程。该框架使用了特定的方式来进行配置，从而使开发人员不再需要定义样板化的配置。

## SpringBoot核心功能

- 独立运行的Spring项目
- 内嵌Servlet容器
- 提供starter简化Maven配置
- 自动配置配置Spring
- 准生产的引用监控
- 无代码生成和xml配置

## SpringBoot的优缺点

### 优点

- 快速构建
- 快速集成主流矿建
- 可独立运行
- 提高开发、部署效率
- 社区活跃、学习资料丰富
- 与云计算天然继承

### 缺点

- 学习门槛、成本较高，入门容易，精通难；



## 第一个StringBoot项目（单模块）

> 本文示例代码：https://github.com/vehang/ehang-spring-boot/tree/main/spring-boot-001-hello-world

### 方式一；官网脚手架

- 第一步，浏览器打开官网脚手架

  地址：http://start.spring.io

- 第二步；选择、填写项目信息

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313140233974.png)

- 第三步，创建并下载项目包

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313141703180.png)

- 第四步，解压并导入项目

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313142740465.png)



### 方式二；IDEA创建

- 第一步，开始创建

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313143840820.png)

- 第二步，选择脚手架

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313143926983.png)

- 第三步，项目配置

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313143914765.png)

- 第四步，加入依赖

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313143908541.png)

- 第五步，完成创建

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313143748095.png)



### Hello World

- 添加依赖

  如果创建项目的时候选择了`Spring Web`依赖，就已经加入了，可自行到pom.xml确认一下是否有以下依赖

  ```xml
  <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  ```

- 创建一个Controller

  ```java
  @RestController
  public class HelloWorldController {
  
      @GetMapping
      public String helloWorld() {
          return "hello world,hello springboot!!!";
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313144742144.png)

- 启动项目

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313145039234.png)

- 验证服务

  浏览器输入：http://127.0.0.1:8080 

  出现下面的返回，说明服务已经正常....

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313145219483.png)



### 单模块打包

前面，通过IDEA工具，启动了服务，但是实际的使用中，需要打成Jar放在服务器运行；

- 打包

  ```shell
  mvn clean package -Dmaven.test.skip=true
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313160924968.png)

- 启动项目

  先进入target目录，然后执行以下命令

  ```shell
  java -jar spring-boot-hello-world-0.0.1-SNAPSHOT.jar
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313161239643.png)

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313161355858.png)

- 验证服务

  同样在浏览器输入：http://127.0.0.1:8080  看是否能成功想要

​		如果成功，这个jar包就可以复制到服务器运行了。



到此，第一个单模块的SpringBoot项目配置完成！



## 多模块项目

通常我们在开发微服务的时候，会将核心模块拆分为多个小的子模块，各自负责一块儿小的功能，来降低整个系统的耦合度；下面就以本教程项目的结构，来教大家如何创建一个多模块的项目，并完成配置及打包工作；

部分结构的示例说明：

- 父模块：ehang-spring-boot

  - 公共模块：spring-boot-005-common
  - 子模块：spring-boot-005-responce-exception  引入spring-boot-005-common
  - 其他子模块...

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321124720490.png)



### 创建多模块

- New Module

  选择父模块，右键，创建`New` --> `Module`

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321215941925.png)

- 创建子模块

  之后的步骤和前面通过IDEA创建项目的步骤一样；

- 调整子模块的依赖

  将父模块的groupId、版本等信息配置到子模块的<parent>中

  ```xml
  <parent>
      <groupId>com.ehang</groupId>
      <artifactId>ehang-spring-boot</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <relativePath>../pom.xml</relativePath> <!-- lookup parent from repository -->
  </parent>
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321222322133.png)

- 父模块中添加<modules>

  将子模块中的name配置到父模块的modules标签中

  ```xml
  <modules>
      <module>spring-boot-005-common</module>
      <module>spring-boot-005-responce-exception</module>
  </modules>
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321222649172.png)

​		

### 多模块打包

#### 配置注意项

- 子模块的<relativePath>

  ```xml
  <relativePath>../pom.xml</relativePath> <!-- lookup parent from repository -->
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321223138978.png)

- Maven插件

  - 子模块配置上插件

    ```xml
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
    ```

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321223416762.png)

  - 父模块移除Maven插件

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321223501731.png)

  - 公共模块移除Maven插件

    `spring-boot-005-common`为公共模块，用于提供给其他模块使用，不单独运行，需要移除Maven插件

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321223646144.png)

  - 添加common模块的依赖

    依赖了`common`的子模块，需要在pom.xml中添加common的依赖配置项

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321223828569.png)

  - packaging配置为pom

    ```xml
    <packaging>pom</packaging>
    ```

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321223951052.png)



#### 打包

```shell
mvn clean package -Dmaven.test.skip=true
```

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321224421802.png)

打包成功之后，各模块的目录下会出现一个`target`文件夹，里面回包含对应的jar包

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220321224551798.png)



多模块和单模块的创建就讲解完了，有任何问题可以随时交流；

最近在整理SpringBoot的学习教程，如果你也在学习SpringBoot，可以[点击查看](https://github.com/vehang/ehang-spring-boot)，期待您的Star

> SpringBoot教程：https://github.com/vehang/ehang-spring-boot



## 联系我

| 公众号                                                       | 微信                                                         | 技术交流群                                                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/gzh_px_2022-03-21+23_04_24.png) | ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/wechat_px_2022-03-21+23_05_51.jpeg) | ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/group_px_2022-03-21+23_07_35.jpeg) |

