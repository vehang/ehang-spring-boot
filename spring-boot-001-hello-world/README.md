![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313162054091.png)

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



## 第一个StringBoot项目

> 本文示例代码：https://github.com/vehang/ehang-spring-boot/tree/main/spring-boot-001-hello-world

### 方式一；官网脚手架

- 第一步，浏览器打开官网脚手架

  地址：http://start.spring.io

- 第二步；选择、填写项目信息

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313140233974.png)

- 第三步，创建并下载项目包

  ![image-20220313141703180](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313141703180.png)

- 第四步，解压并导入项目![image-20220313142740465](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220313142740465.png)



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



## Hello World

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



## 打包

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



到此，第一个SpringBoot项目配置完成！



最近在整理SpringBoot的学习教程，如果你也在学习SpringBoot，可以[点击查看](https://github.com/vehang/ehang-spring-boot)，期待您的Star

> 本文示例教程：https://github.com/vehang/ehang-spring-boot/tree/main/spring-boot-001-hello-world
>
> SpringBoot教程：https://github.com/vehang/ehang-spring-boot



