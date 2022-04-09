![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/%E9%BB%98%E8%AE%A4%E6%96%87%E4%BB%B61638193951524.png)

大家好，我是一航！

程序员每天的搬砖日常，可以说CURD占据了绝大部分的工作；因此，数据库的CURD也就占据了很大一部分的工作时间，不是在配置xml，就是在写sql的路上；

那有没有什么方式能否把这份苦力活给替代了呢？当然是有的，也就是今天介绍的2框框架+1个工具（`MyBatis Plus` + `MyBatisX` + `MyBatis Plus Join`）；不写一行数据库操作代码，不加一行配置文件；**一键生成代码**，`基础的CURD`、`联表查询`API统统搞定；让我们可以安心将精力完全放在产品业务逻辑开发上。

开整！！！

## 目录说明

- 框架、工具介绍
- 数据库相关配置
- MybatisX代码自动生成
- MyBatis Plus使用
  - 结构说明
  - Service的CURD操作
  - 条件构造器
- MyBatis Plus Join联表查询
- 总结



## 框架、工具介绍

- MyBatis Plus

  [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)（简称 MP）是一个 [MyBatis](https://www.mybatis.org/mybatis-3/)的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。

  > 官网地址：https://mp.baomidou.com/

- MyBatis Plus Join

  一款对MyBatis Plus 扩展的框架，在其基础上增加了联表查询相关的API；

  > https://gitee.com/best_handsome/mybatis-plus-join

- MyBatisX

  MybatisX 是一款基于 IDEA 的快速开发插件，为效率而生。用于一键生成ORM代码；该插件在本文中的主要目的是为了快速生成基于MyBatis Plus相关的代码；

接下来就要开始对框架和工具的实战运用了；

## 导入依赖

- 必备

  ```xml
  <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus-boot-starter</artifactId>
      <version>3.4.3.4</version>
  </dependency>
  ```

  数据库连接依赖；大版本务必和自己的数据库版本一致

  ```xml
  <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.46</version>
  </dependency>
  ```

  分页

  ```xml
  <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus-extension</artifactId>
      <version>3.4.1</version>
  </dependency>
  ```

  联表查询

  ```xml
  <!--https://gitee.com/best_handsome/mybatis-plus-join-->
  <dependency>
      <groupId>com.github.yulichang</groupId>
      <artifactId>mybatis-plus-join</artifactId>
      <version>1.1.8</version>
  </dependency>
  ```

- 辅助

  ```xml
  <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid-spring-boot-starter</artifactId>
      <version>1.1.9</version>
  </dependency>
  
  <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
  </dependency>
  
  <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>fastjson</artifactId>
      <version>1.2.73</version>
  </dependency>
  ```



## 数据库配置

本文涉及到的表以及测试数据的sql脚本，[点击下载](https://github.com/vehang/ehang-spring-boot/tree/main/spring-boot-010-mysql-mybatis-plus/src/main/resources/sql/ehang.sql)

- 数据库表

  一张简单的用户数据表

  ```sql
  CREATE TABLE `user_info`  (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
    `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
    `source` tinyint(4) NULL DEFAULT NULL COMMENT '来源',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `source_id`(`source`) USING BTREE
  ) ENGINE = InnoDB AUTO_INCREMENT = 1008 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
  ```

- SpringBoot数据库配置

  ```properties
  spring:
    application:
      name: ehang-mybatis-plus
    #数据库连接相关配置
    datasource:
      url: jdbc:mysql://192.168.1.237:3306/ehang?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=GMT%2B8&useSSL=false
      username: root
      password: 123456
      #阿里巴巴的druid的mysql连接池
      type: com.alibaba.druid.pool.DruidDataSource
      driver-class-name: com.mysql.jdbc.Driver
  ```

- 启动类配置Dao扫描

  其中`basePackages`路径，请根据个人的实际情况填写；

  ```java
  @SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
  @MapperScan(basePackages = {"com.ehang.mysql.mybatis.plus.generator.**.mapper"})
  ```




## MybatisX

MybatisX 是一款基于 IDEA 的快速开发插件，为效率而生。**一键生成ORM代码**；结合MyBatis Plus，生成的代码就已经具备了数据库增删改查的基本功能，直接去开发业务功能就好了；

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/2021112921220000.gif)

插件使用步骤如下：

- **安装插件**

  安装方法：打开 IDEA，进入 File -> Settings -> Plugins -> Browse Repositories，输入 `mybatisx` 搜索并安装。

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211031002906555.png)

- **配置数据源**

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211031003116332.png)

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211031003319490.png)

- 自动生成ORM代码

  - 第一步

    选中表（支持多选），`右键`选择“`MybatisX-Generator`”

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211031004356738.png)

  - 配置基础信息

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211031005130186.png)

  - 属性、方法配置

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211031010547038.png)

  - 生成后的效果

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211031010724573.png)





## MyBatis Plus使用

> 官网：https://mp.baomidou.com/
>
> 官方示例：https://github.com/baomidou/mybatis-plus-samples

### 结构说明

上面介绍的工具（`MyBatisX`）已经帮我们基于MyBatis Plus3生成好了数据库操作的基础CURD代码，先一起来简单看一下有那些内容

- **demain**

  用于接收数据库数据的Java实体类

- **UserInfoMapper.xml**

  指明Java实体类与数据库表之间的映射关系

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.ehang.mysql.mybatis.plus.generator.user.mapper.UserInfoMapper">
  
      <resultMap id="BaseResultMap" type="com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo">
              <id property="id" column="id" jdbcType="INTEGER"/>
              <result property="userName" column="user_name" jdbcType="VARCHAR"/>
              <result property="age" column="age" jdbcType="INTEGER"/>
              <result property="source" column="source" jdbcType="TINYINT"/>
      </resultMap>
  
      <sql id="Base_Column_List">
          id,user_name,age,
          source
      </sql>
  </mapper>
  ```

- **mapper**

  数据库操作的Mapper，继承了MyBatis Plus的`BaseMapper`

  ```java
  public interface UserInfoMapper extends BaseMapper<UserInfo> {
  }
  ```

  **BaseMapper帮我们做了大量的数据库基础操作**，详情如下：

  ```java
  public interface BaseMapper<T> extends Mapper<T> {
      int insert(T entity);
  
      int deleteById(Serializable id);
  
      int deleteByMap(@Param("cm") Map<String, Object> columnMap);
  
      int delete(@Param("ew") Wrapper<T> queryWrapper);
  
      int deleteBatchIds(@Param("coll") Collection<? extends Serializable> idList);
  
      int updateById(@Param("et") T entity);
  
      int update(@Param("et") T entity, @Param("ew") Wrapper<T> updateWrapper);
  
      T selectById(Serializable id);
  
      List<T> selectBatchIds(@Param("coll") Collection<? extends Serializable> idList);
  
      List<T> selectByMap(@Param("cm") Map<String, Object> columnMap);
  
      T selectOne(@Param("ew") Wrapper<T> queryWrapper);
  
      Integer selectCount(@Param("ew") Wrapper<T> queryWrapper);
  
      List<T> selectList(@Param("ew") Wrapper<T> queryWrapper);
  
      List<Map<String, Object>> selectMaps(@Param("ew") Wrapper<T> queryWrapper);
  
      List<Object> selectObjs(@Param("ew") Wrapper<T> queryWrapper);
  
      <E extends IPage<T>> E selectPage(E page, @Param("ew") Wrapper<T> queryWrapper);
  
      <E extends IPage<Map<String, Object>>> E selectMapsPage(E page, @Param("ew") Wrapper<T> queryWrapper);
  }
  ```

- **Service**

  service层的基础接口，继承了MyBatis Plus的`IService`，定义了众多基础的Service接口，由于内容较多，这里就不贴出来了，可以自行查看`IService`接口的定义；

  如果自动生成的接口无法满足业务需求的时候，也可以在这里定义接口，来满足个性化的需要。

  ```java
  public interface UserInfoService extends IService<UserInfo> {
  }
  ```
  
- **ServiceImpl**

  继承了MyBatis Plus 的`ServiceImpl`，ServiceImpl基于BaseMapper实现了IService定义的基础的接口
  
  ```java
  public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
      implements UserInfoService{
  }
  ```
  
  

到此，三个简单的类，CURD相关的`Service`层、`Dao`层功能就已经全部有了；如果是非特殊情况，这些API已经足够我们去做业务功能开发了。

 

### Service的CURD功能

基本的结构了解清楚之后，就一起来看看，IService到底帮我们提供了那些API，这些API又要如何去使用；

#### API列表

| API          | 功能         | 描述                                 |
| ------------ | ------------ | ------------------------------------ |
| save         | 添加、保存   | 支持单条和批量                       |
| saveOrUpdate | 添加或者修改 | 主键不存在就添加，否则就基于主键修改 |
| remove       | 删除数据     | 条件删除、主键删除、批量删除         |
| update       | 修改         | 支持单条修改、批量修改               |
| get          | 查询单条记录 |                                      |
| list         | 批量查询     | 批量查询                             |
| page         | 分页查询     | 需要分页插件的支持                   |
| count        | 记录数       | 查询总数、满足条件的记录数           |
| chain        | 流式调用     | 让API调用更加方便简单                |

#### save

插入功能

- API列表

  ```java
  // 插入一条记录（选择字段，策略插入）
  boolean save(T entity);
  // 插入（批量）
  boolean saveBatch(Collection<T> entityList);
  // 插入（批量） batchSize指明单批次最大数据量，批量插入数量较大时，推荐使用这个
  boolean saveBatch(Collection<T> entityList, int batchSize);
  ```

- 代码

  ```java
  // 单个插入
  @Test
  void save() {
      UserInfo userInfo = new UserInfo(null, "张三", 10, (byte) 1);
      boolean save = userInfoService.save(userInfo);
      log.info("单条添加的结果：{}", save);
  }
  
  // 批量插入
  @Test
  void saveBatch() {
      UserInfo lisi = new UserInfo(null, "李四", 10, (byte) 1);
      UserInfo wangwu = new UserInfo(null, "王五", 10, (byte) 1);
      List<UserInfo> userInfos = new ArrayList<>();
      userInfos.add(lisi);
      userInfos.add(wangwu);
      boolean saveBatch = userInfoService.saveBatch(userInfos, 10);
      log.info("批量添加的结果：{}", saveBatch);
  }
  ```

- 测试结果

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211031180902433.png)

#### SaveOrUpdate

插入，如果数据存在则修改

- API列表

  ```java
  // TableId 注解存在更新记录，否插入一条记录
  boolean saveOrUpdate(T entity);
  // 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
  boolean saveOrUpdate(T entity, Wrapper<T> updateWrapper);
  // 批量修改插入
  boolean saveOrUpdateBatch(Collection<T> entityList);
  // 批量修改插入
  boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize);
  ```

- 测试代码

  ```java
  @Test
  void saveOrUpdate() {
      // 单个修改
      UserInfo userInfo = new UserInfo(1004, "张三(改)", 20, (byte) 1);
      boolean saveOrUpdate = userInfoService.saveOrUpdate(userInfo);
      log.info("单条插入(或修改)的结果：{}", saveOrUpdate);
  
      // 根据条件修改
      LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
      updateWrapper.eq(UserInfo::getSource, 1);
      boolean saveOrUpdateByWrapper = userInfoService.saveOrUpdate(userInfo, updateWrapper);
      log.info("单条插入(或根据条件修改)的结果：{}", saveOrUpdateByWrapper);
  
      // 批量插入
      UserInfo lisi = new UserInfo(1005, "李四（改）", 10, (byte) 1);
      UserInfo wangwu = new UserInfo(1006, "王五（改）", 10, (byte) 1);
      List<UserInfo> userInfos = new ArrayList<>();
      userInfos.add(lisi);
      userInfos.add(wangwu);
      boolean saveBatch = userInfoService.saveOrUpdateBatch(userInfos, 10);
      log.info("批量插入(或修改)的结果：{}", saveBatch);
  }
  ```

- 执行结果

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211031182205764.png)



#### remove

删除数据

- API列表

  ```java
  // 根据 entity 条件，删除记录
  boolean remove(Wrapper<T> queryWrapper);
  // 根据 ID 删除
  boolean removeById(Serializable id);
  // 根据 columnMap 条件，删除记录
  boolean removeByMap(Map<String, Object> columnMap);
  // 删除（根据ID 批量删除）
  boolean removeByIds(Collection<? extends Serializable> idList);
  ```

- 测试代码

  ```java
      @Test
      void remove() {
          // 根据条件删除
          LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
          queryWrapper.eq(UserInfo::getUserName, "张三");
          boolean remove = userInfoService.remove(queryWrapper);
          log.info("根据条件删除用户数据：{}", remove);
      }
  
      @Test
      void removeById() {
          // 根据主键id删除
          boolean removeById = userInfoService.removeById(1006);
          log.info("根据主键ID删除用户数据：{}", removeById);
  
      }
  
      @Test
      void removeByMap() {
          // 根据列的值删除
          Map<String, Object> cms = new HashMap();
          cms.put("user_name", "李四");
          cms.put("source", 1);
          boolean removeByMap = userInfoService.removeByMap(cms);
  
          log.info("根据字段值删除用户数据：{}", removeByMap);
      }
  
      @Test
      void removeByIds() {
          // 根据主键id批量删除
          List<Integer> ids = Arrays.asList(new Integer[]{1004, 1005, 1006});
          boolean removeByIds = userInfoService.removeByIds(ids);
          log.info("根据主键ids批量删除用户数据：", removeByIds);
      }
  ```

- 测试结果

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211031192259163.png)



#### update

修改数据

- API列表

  ```java
  // 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
  boolean update(Wrapper<T> updateWrapper);
  // 根据 whereWrapper 条件，更新记录
  boolean update(T updateEntity, Wrapper<T> whereWrapper);
  // 根据 ID 选择修改
  boolean updateById(T entity);
  // 根据ID 批量更新
  boolean updateBatchById(Collection<T> entityList);
  // 根据ID 批量更新
  boolean updateBatchById(Collection<T> entityList, int batchSize);
  ```

- 测试代码

  ```java
  @SpringBootTest
  @Slf4j
  public class UpdateTest {
      @Autowired
      UserInfoService userInfoService;
  
      @Test
      public void update() {
          // 不建议使用，有
          // 以下的setSql和set选一个即可，务必要设置条件 否则有全部修改的风险
          //updateWrapper.setSql("user_name = '张三'");
          LambdaUpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<UserInfo>()
                  .lambda()
                  .set(UserInfo::getUserName, "一行Java（改1）")
                  .eq(UserInfo::getId, 1);
          boolean update = userInfoService.update(updateWrapper);
          log.info("根据UpdateWrapper修改（不推荐使用）：{}", update);
      }
  
      @Test
      public void update2() {
          // 将符合UpdateWrapper全部修改为entity的值
          LambdaUpdateWrapper<UserInfo> updateWrapper1 = new UpdateWrapper<UserInfo>()
                  .lambda()
                  .eq(UserInfo::getUserName, "一行Java（改1）");
          UserInfo wangwu = new UserInfo(1, "一行Java（改2）", 10, (byte) 1);
          boolean update = userInfoService.update(wangwu, updateWrapper1);
          log.info("根据UpdateWrapper修改为指定对象：{}", update);
      }
  
      // 根据对象ID进行修改
      @Test
      public void updateById() {
          UserInfo wangwu = new UserInfo(1, "一行Java（改2）", 10, (byte) 1);
          boolean update = userInfoService.updateById(wangwu);
          log.info("根据对象ID修改：{}", update);
      }
  
      // 根据ID批量修改数据
      @Test
      public void updateBatchById() {
          UserInfo u1 = new UserInfo(1, "一行Java 1", 10, (byte) 1);
          UserInfo u2 = new UserInfo(2, "一行Java 2", 20, (byte) 1);
          UserInfo u3 = new UserInfo(3, "一行Java 3", 30, (byte) 1);
          List<UserInfo> us = new ArrayList<>();
          us.add(u1);
          us.add(u2);
          us.add(u3);
          boolean update = userInfoService.updateBatchById(us);
          log.info("根据对象ID批量修改：{}", update);
      }
  
      // 根据ID批量修改数据,每个批次的数量由后面的batchSize指定
      @Test
      public void updateBatchById2() {
          UserInfo u1 = new UserInfo(1, "一行Java 1", 10, (byte) 1);
          UserInfo u2 = new UserInfo(2, "一行Java 2", 20, (byte) 1);
          UserInfo u3 = new UserInfo(3, "一行Java 3", 30, (byte) 1);
          List<UserInfo> us = new ArrayList<>();
          us.add(u1);
          us.add(u2);
          us.add(u3);
          boolean update = userInfoService.updateBatchById(us, 2);
          log.info("根据对象ID批量修改：{}", update);
      }
  }
  ```

- 测试结果

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211123225711568.png)



#### Get

获取单条记录

- API列表

  ```java
  // 根据 ID 查询
  T getById(Serializable id);
  // 根据 Wrapper，查询一条记录。结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
  T getOne(Wrapper<T> queryWrapper);
  // 根据 Wrapper，查询一条记录
  T getOne(Wrapper<T> queryWrapper, boolean throwEx);
  // 根据 Wrapper，查询一条记录
  Map<String, Object> getMap(Wrapper<T> queryWrapper);
  // 根据 Wrapper，查询一条记录
  <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
  ```

- 测试代码

  ```java
  @SpringBootTest
  @Slf4j
  public class GetTest {
      @Autowired
      UserInfoService userInfoService;
  
      @Test
      void getById() {
          UserInfo userInfo = userInfoService.getById(1);
          log.info("根据ID查询用户信息:{}", userInfo);
      }
  
      // 查询一条数据，如果根据条件查询出了多条，则会报错
      @Test
      void getOne() {
          LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                  .lambda()
                  .eq(UserInfo::getId, 1);
          UserInfo userInfo = userInfoService.getOne(lambdaQueryWrapper);
          log.info("根据ID查询单用户信息:{}", userInfo);
      }
  
      // 查询单条数据，如果返回多条数据则去取第一条返回
      @Test
      void getOne2() {
          LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                  .lambda()
                  .eq(UserInfo::getUserName, "一行Java 1")
                  .orderByDesc(UserInfo::getId);
          UserInfo userInfo = userInfoService.getOne(lambdaQueryWrapper, false);
          log.info("根据ID查询单用户信息:{}", userInfo);
      }
  
      // 查询单条数据 以Map的方式返回
      @Test
      void getMap() {
          LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                  .lambda()
                  .eq(UserInfo::getId, 1);
          // String为数据库列名  Object为值
          Map<String, Object> map = userInfoService.getMap(lambdaQueryWrapper);
          log.info("根据ID查询单用户信息:{}", map);
      }
  
      // 查询返回结果的第一列
      @Test
      void getObj() {
          LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                  .lambda()
                  .eq(UserInfo::getUserName, "一行Java 1")
                  .select(UserInfo::getUserName);
  
          String obj = userInfoService.getObj(lambdaQueryWrapper, (u) -> u.toString());
          log.info("getObj:{}", obj);
      }
  }
  ```

  执行结果

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211123235607256.png)

#### List

批量查询

- API列表

  ```java
  // 查询所有
  List<T> list();
  // 查询列表
  List<T> list(Wrapper<T> queryWrapper);
  // 查询（根据ID 批量查询）
  Collection<T> listByIds(Collection<? extends Serializable> idList);
  // 查询（根据 columnMap 条件）
  Collection<T> listByMap(Map<String, Object> columnMap);
  // 查询所有列表
  List<Map<String, Object>> listMaps();
  // 查询列表
  List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper);
  // 查询全部记录
  List<Object> listObjs();
  // 查询全部记录
  <V> List<V> listObjs(Function<? super Object, V> mapper);
  // 根据 Wrapper 条件，查询全部记录
  List<Object> listObjs(Wrapper<T> queryWrapper);
  // 根据 Wrapper 条件，查询全部记录
  <V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
  ```

- 测试代码

  ```java
  package com.ehang.springboot.mybatisplus;
  
  import com.alibaba.fastjson.JSON;
  import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
  import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
  import lombok.extern.slf4j.Slf4j;
  import org.junit.jupiter.api.Test;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.boot.test.context.SpringBootTest;
  
  import java.util.Map;
  
  @SpringBootTest
  @Slf4j
  public class PageTest {
  
      @Autowired
      UserInfoService userInfoService;
  
      @Test
      void page() {
          // 分页查询；结果以对象方式返回
          Page<UserInfo> page = userInfoService.page(new Page<UserInfo>(2, 5));
          log.info("page:{}", page);
      }
  
      @Test
      void pageByWrapper() {
          // 带查询条件的分页查询; 结果以对象方式返回
          // 查询条件是id大于10
          LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                  .lambda()
                  .ge(UserInfo::getId, 10);
          Page<UserInfo> page = userInfoService.page(new Page<UserInfo>(2, 5), lambdaQueryWrapper);
          log.info(":{}", page);
      }
  
      @Test
      void pageMaps() {
          // 分页查询；以Map的方式返回
          Page<Map<String, Object>> page = userInfoService.pageMaps(new Page(2, 5));
          log.info("pageMaps:{}", JSON.toJSONString(page));
      }
  
      @Test
      void pageMapsByWrapper() {
          // 带查询条件的分页查询，结果以Map方式返回
          // 查询条件是id大于10
          LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                  .lambda()
                  .ge(UserInfo::getId, 10);
          Page<Map<String, Object>> page = userInfoService.pageMaps(new Page(2, 5), lambdaQueryWrapper);
          log.info("pageMapsByWrapper:{}", JSON.toJSONString(page));
      }
  }
  ```

  执行结果

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128131723898.png)

#### page

分页查询

- 分页插件

  ```xml
  <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus-extension</artifactId>
      <version>3.4.1</version>
  </dependency>
  ```

- 分页插件配置

  ```java
      // 新版
      @Bean
      public MybatisPlusInterceptor mybatisPlusInterceptor() {
          MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
          interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
          return interceptor;
      }
  
  //    // 旧版
  //    @Bean
  //    public PaginationInterceptor paginationInterceptor() {
  //        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
  //        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
  //        // paginationInterceptor.setOverflow(false);
  //        // 设置最大单页限制数量，默认 500 条，-1 不受限制
  //        // paginationInterceptor.setLimit(500);
  //        // 开启 count 的 join 优化,只针对部分 left join
  //        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
  //        return paginationInterceptor;
  //    }
  ```

- API列表

  ```java
  // 无条件分页查询
  IPage<T> page(IPage<T> page);
  // 条件分页查询
  IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper);
  // 无条件分页查询
  IPage<Map<String, Object>> pageMaps(IPage<T> page);
  // 条件分页查询
  IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper);
  ```

- 测试代码

  ```java
  @SpringBootTest
  @Slf4j
  public class PageTest {
  
      @Autowired
      UserInfoService userInfoService;
  
      @Test
      void page() {
          // 分页查询；结果以对象方式返回
          Page<UserInfo> page = userInfoService.page(new Page<UserInfo>(2, 5));
          log.info("page:{}", page);
      }
  
      @Test
      void pageByWrapper() {
          // 带查询条件的分页查询; 结果以对象方式返回
          // 查询条件是id大于10
          LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                  .lambda()
                  .ge(UserInfo::getId, 10);
          Page<UserInfo> page = userInfoService.page(new Page<UserInfo>(2, 5), lambdaQueryWrapper);
          log.info("pageByWrapper:{}", page);
      }
  
      @Test
      void pageMaps() {
          // 分页查询；以Map的方式返回
          Page<Map<String, Object>> page = userInfoService.pageMaps(new Page(2, 5));
          log.info("pageMaps:{}", JSON.toJSONString(page));
      }
  
      @Test
      void pageMapsByWrapper() {
          // 带查询条件的分页查询，结果以Map方式返回
          // 查询条件是id大于10
          LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                  .lambda()
                  .ge(UserInfo::getId, 10);
          Page<Map<String, Object>> page = userInfoService.pageMaps(new Page(2, 5), lambdaQueryWrapper);
          log.info("pageMapsByWrapper:{}", JSON.toJSONString(page));
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128215434998.png)

#### Count

查询记录数

- API列表

  ```java
  // 查询总记录数
  int count();
  // 根据 Wrapper 条件，查询总记录数
  int count(Wrapper<T> queryWrapper);
  ```

- 测试代码

  ```java
  @SpringBootTest
  @Slf4j
  public class CountTest {
      @Autowired
      UserInfoService userInfoService;
  
      @Test
      void count() {
          int count = userInfoService.count();
          log.info("总数：{}", count);
      }
  
      @Test
      void countByWrapper() {
          int count = userInfoService.count(new QueryWrapper<UserInfo>()
                  .lambda()
                  .ge(UserInfo::getId, 100));
          log.info("按条件查询总数：{}", count);
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128215857688.png)



#### Chain（重要）

service的链式操作，这个是实际使用中会用的比较频繁的API，让我们在写代码时，调用API的操作更加的优雅；

- API列表

  ```java
  // 链式查询 普通
  QueryChainWrapper<T> query();
  // 链式查询 lambda 式。注意：不支持 Kotlin
  LambdaQueryChainWrapper<T> lambdaQuery(); 
  
  // 链式更改 普通
  UpdateChainWrapper<T> update();
  // 链式更改 lambda 式。注意：不支持 Kotlin 
  LambdaUpdateChainWrapper<T> lambdaUpdate();
  ```

- 测试代码

  ```java
  @SpringBootTest
  @Slf4j
  public class ChainTest {
      @Autowired
      UserInfoService userInfoService;
  
      @Test
      void chainQuery() {
          List<UserInfo> userInfos = userInfoService
                  .query()
                  .eq("user_name", "一行Java 1")
                  .list();
          log.info("流式查询：{}", JSON.toJSONString(userInfos));
      }
  
      @Test
      void chainLambdaQuery() {
          List<UserInfo> userInfos = userInfoService
                  .lambdaQuery()
                  .eq(UserInfo::getUserName, "一行Java 1")
                  .list();
          log.info("流式查询：{}", JSON.toJSONString(userInfos));
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128220520313.png)



Service相关的API基本已经演示完毕了，在示例代码中，也见到了一些常用的条件构造器，比如`eq`、`ge`等，但条件构造器远不止这么一点点；MyBatis Plus 给所有的条件构造都提供了详细的API支持



### 条件构造器

#### 构造器详细列表

下面通过一张表格，来完整的看一下所有条件构造器的方法；

| 关键字      | 作用               | 示例                                                         | 等价SQL                                                      |
| ----------- | ------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| allEq       | 匹配所有字段全部eq | .query().allEq({id:1,user_name:"老王",age:null}).list()      | WHERE id =1 AND user_neme="老王" AND age IS NULL             |
| eq          | 等于（==）         | .lambdaQuery().eq(UserInfo::getId, 1)                        | WHERE id = 1                                                 |
| ne          | 不等于（<>）       | .lambdaQuery().ne( UserInfo::getId, 1)                       | WHERE id <> 1                                                |
| gt          | 大于（>）          | .lambdaQuery().gt( UserInfo::getId, 1)                       | WHERE id > 1                                                 |
| ge          | 大于等于（>=）     | .lambdaQuery().ge( UserInfo::getId, 1)                       | WHERE id >= 1                                                |
| lt          | 小于（<）          | .lambdaQuery().lt( UserInfo::getId, 1)                       | WHERE id < 1                                                 |
| le          | 小于等于（<=）     | .lambdaQuery().le( UserInfo::getId, 1)                       | WHERE id <= 1                                                |
| between     | 指定区间内         | .lambdaQuery().between( UserInfo::getId, 1,10)               | WHERE (id BETWEEN 1 AND 10)                                  |
| notBetween  | 指定区间外         | .lambdaQuery().notBetween( UserInfo::getId, 5,100)           | WHERE (id NOT BETWEEN 5 AND 100)                             |
| like        | 字符串匹配         | .lambdaQuery().like( UserInfo::getUserName, “一行Java”)      | WHERE (user_name LIKE "%一行Java%")                          |
| notLike     | 字符串不匹配       | .lambdaQuery().notLike( UserInfo::getUserName, “一行Java”)   | WHERE (user_name NOT LIKE "%一行Java%")                      |
| likeLeft    | 字符串左匹配       | .lambdaQuery().likeLeft( UserInfo::getUserName, “一行Java”)  | WHERE (user_name LIKE "%一行Java")                           |
| likeRight   | 字符串右匹配       | .lambdaQuery().likeRight( UserInfo::getUserName, “一行Java”) | WHERE (user_name LIKE "一行Java%")                           |
| isNull      | 等于null           | .lambdaQuery().isNull(UserInfo::getUserName)                 | WHERE (user_name IS NULL)                                    |
| isNotNull   | 不等于null         | .lambdaQuery().isNotNull( UserInfo::getUserName)             | WHERE (user_name IS NOT NULL)                                |
| in          | 包含               | .lambdaQuery().in(UserInfo::getId, 1, 2, 3)                  | WHERE (id IN (1, 2, 3))                                      |
| notIn       | 不包含             | .lambdaQuery().notIn(UserInfo::getId, 1, 2, 3)               | WHERE (id NOT IN (1, 2, 3))                                  |
| inSql       | sql方式包含        | .lambdaQuery().inSql(UserInfo::getId, "1, 2, 3")             | WHERE (id IN (1, 2, 3))                                      |
| notInSql    | sql方式不包含      | .lambdaQuery().notInSql(UserInfo::getId, "1, 2, 3")          | WHERE (id NOT IN (1, 2, 3))                                  |
| groupBy     | 分组               | .select("source,count(id) as sum").groupBy("source").having("count(id) > {0}", 35); | GROUP BY source HAVING count(id) > 35                        |
| orderByAsc  | 升序               | .lambdaQuery().orderByAsc(UserInfo::getSource)               | ORDER BY source ASC                                          |
| orderByDesc | 降序               | .lambdaQuery().orderByDesc(UserInfo::getSource)              | ORDER BY source DESC                                         |
| orderBy     | 排序               | .lambdaQuery().orderBy(true, true, UserInfo::getSource)      | ORDER BY source ASC                                          |
| having      | having子句         | .select("source,count(id) as sum").groupBy("source").having("count(id) > {0}", 35); | GROUP BY source HAVING count(id) > 35                        |
| func        | 自定义Consumer     | .lambdaQuery().func(i -> {if (true) {i.eq(UserInfo::getId, 10);} else {i.eq(UserInfo::getId, 100); }}) | WHERE (id = 10)                                              |
| or          | 多条件满足一个     | .lambdaQuery()..le(UserInfo::getId, 10) .or(i -> .eq(UserInfo::getUserName, "张三").ge(UserInfo::getId, 1005)) | WHERE (id <= 10 OR (user_name = "张三" AND id >= 1005))      |
| and         | 多条件同时满足     | .lambdaQuery().le(UserInfo::getId, 10) .and(i -> i.eq(UserInfo::getUserName, "一行Java 1")) | WHERE (id <= 10 AND (user_name = "一行Java 1"))              |
| nested      | 指定条件用()嵌套   | .lambdaQuery().ge(UserInfo::getId, 10).nested(i -> i.eq(UserInfo::getUserName, "张三").or(m -> m.ge(UserInfo::getId, 1005))) | WHERE (id >= 10 AND (user_name = "张三" OR (id >= 1005)))    |
| apply       | 拼接sql            | .lambdaQuery().apply("id < {0}", 20)                         | WHERE (id < 20)                                              |
| last        | 拼接语句在sql最后  | .lambdaQuery().apply("id < {0}", 20).last("limit 1")         | WHERE (id < ?) limit 1                                       |
| exists      | 子句存在数据       | .lambdaQuery().exists("select id from user_info where id > 1000") | WHERE (EXISTS (select id from user_info where id > 1000))    |
| notExists   | 子句不存在数据     | .lambdaQuery().notExists("select id from user_info where id > 10000") | WHERE (NOT EXISTS (select id from user_info where id > 10000)) |

通过上面的表格，再结合`示例代码`以及`等价SQL`就能很清晰的看出各个条件构造器的功能了；

**下面拧几个不好理解或者需要注意的构造器，专门说一下**

#### allEq

- 参数

  - condition

    所有条件是否生效，默认是true；设置为false之后，设置的所有的条件都不会生效

  - params

    Map参数；设置需要匹配的字段和对应的值

  - filter

    用于设置需要过滤的字段

  - null2IsNull

    是否忽略null值；默认是true，如果有需要匹配的字段是null，则会添加 is null的查询条件；如果设置为false，将会自动剔除所有值null的字段校验
  
- 测试代码

  ```java
  /**
   * AllEq
   */
  @SpringBootTest
  @Slf4j
  public class AllEqTest {
      @Autowired
      UserInfoService userInfoService;
  
      Map<String, Object> params = new HashMap();
  
      @BeforeEach
      public void init() {
          params.put("user_name", "一行Java 1");
          params.put("id", null);
      }
  
      @Test
      void allEq() {
          List<UserInfo> list = userInfoService.query().allEq(params).list();
          log.info("{}", JSON.toJSONString(list));
          // 等价sql: SELECT id,user_name,age,source FROM user_info WHERE (user_name = "一行Java 1" AND id IS NULL)
      }
  
      @Test
      void allEqConditionFalse() {
          //-----------condition参数------------
          // 表示是否才上查询条件，如果等于false 将不会添加任何查询条件
          List<UserInfo> list = userInfoService.query().allEq(false, params, true).list();
          log.info("{}", JSON.toJSONString(list));
          // 等价sql: SELECT id,user_name,age,source FROM user_info
      }
  
      @Test
      void allEqNull2IsNull() {
          //-----------null2IsNull演示------------
          // null2IsNull = false;会自动踢出null值条件
          List<UserInfo> list = userInfoService.query().allEq(params, false).list();
          // 等价sql: SELECT id,user_name,age,source FROM user_info WHERE (user_name = "一行Java 1")
          log.info("{}", JSON.toJSONString(list));
      }
  
      @Test
      void allEqFilter() {
          //---------filter延时----------
          // filter字段，表示要忽略的字段
          // 以下是忽略key为id的条件
          List<UserInfo> list = userInfoService.query().allEq((k, v) -> !k.equals("id"), params).list();
          // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (user_name = "一行Java 1")
          log.info("{}", JSON.toJSONString(list));
      }
  }
  ```
  
  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128193936403.png)
  
    

#### groupBy and having

跟组跟having筛选

- 示例代码

  ```java
  @SpringBootTest
  @Slf4j
  public class GroupByAndHavingTest {
      @Autowired
      UserInfoService userInfoService;
  
      @Test
      void groupByAndHaving() {
          QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
          userInfoQueryWrapper.select("source,count(id) as sum")
                  .groupBy("source")
                  .having("count(id) > {0}", 35);
          List<Map<String, Object>> maps = userInfoService.listMaps(userInfoQueryWrapper);
  
          // 等价sql：SELECT source,count(id) as sum FROM user_info GROUP BY source HAVING count(id) > 35
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128194340559.png)

#### func

**用于设置条件子句**；

实际的业务场景下，可能存在不同的业务条件下导致的sql执行条件也有所不同；那么就可以通过`func`子句来进行设置

- 测试代码

  ```java
  @SpringBootTest
  @Slf4j
  public class FuncTest {
      @Autowired
      UserInfoService userInfoService;
  
      @Test
      void func() {
          Boolean condition = true;
          List<UserInfo> userInfos = userInfoService.lambdaQuery()
                  .func(i -> {
                      if (condition) {
                          i.eq(UserInfo::getId, 10);
                      } else {
                          i.eq(UserInfo::getId, 100);
                      }
                  }).list();
          log.info("userInfos:{}", userInfos);
          //func(i -> {if (true) {i.eq(UserInfo::getId, 10);} else {i.eq(UserInfo::getId, 100); }})
      }
  }
  ```

- 执行结果

  - Boolean condition = true；

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128194712145.png)

  - Boolean condition = false；

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128194806206.png)

#### or 、 and

or：多条件满足一个即可

and：多条件同时满足

- 示例代码

  ```java
  @SpringBootTest
  @Slf4j
  public class OrAndTest {
      @Autowired
      UserInfoService userInfoService;
  
      @Test
      void or() {
          List<UserInfo> userInfos = userInfoService.lambdaQuery()
                  .le(UserInfo::getId, 10)
                  .or(i -> i.eq(UserInfo::getUserName, "张三").ge(UserInfo::getId, 1005))
                  .list();
          log.info("userInfo:{}", userInfos);
          // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id <= 10 OR (user_name = "张三" AND id >= 1005))
      }
  
      @Test
      void and() {
          List<UserInfo> userInfos = userInfoService.lambdaQuery()
                  .le(UserInfo::getId, 10)
                  .and(i -> i.eq(UserInfo::getUserName, "一行Java 1")).list();
          log.info("userInfo:{}", userInfos);
          // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id <= 10 AND (user_name = "一行Java 1"))
      }
  }
  ```

  - or

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128195355573.png)

  - and

    ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128195322109.png)



#### nested、apply、last

- nested

  嵌套；

  比如当条件中存在and和or组合的时候，就需要对or的多个条件进行嵌套，防止与and之间产生错误的组合关系

- apply

  拼接sql；有些特殊个性化场景下，很难用api去定义一些操作；比如，需要对时间继续格式化之后作为查询条件，此时就需要借助一段简单的sql拼接来完成效果

  ```java
  apply("date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")`--->`date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")
  ```

- last

  在sql的末尾带上指定的语句；比如`last("limit 1")`，就会在sql语句的末尾加上`limit 1`

- API列表

  ```java
  // nested
  nested(Consumer<Param> consumer)
  nested(boolean condition, Consumer<Param> consumer)
  
  // apply
  apply(String applySql, Object... params)
  apply(boolean condition, String applySql, Object... params)
  
  // last
  last(String lastSql)
  last(boolean condition, String lastSql)
  ```

- 示例代码

  ```java
  @SpringBootTest
  @Slf4j
  public class Nested_Apply_Limit_Test {
      @Autowired
      UserInfoService userInfoService;
  
      @Test
      void nested() {
          List<UserInfo> userInfos = userInfoService.lambdaQuery()
                  .ge(UserInfo::getId, 10)
                  .nested(
                          i -> i.eq(UserInfo::getUserName, "张三").or(m -> m.ge(UserInfo::getId, 1005))
                  )
                  .list();
          log.info("userInfo:{}", userInfos);
          // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id <= 10 OR (user_name = "张三" AND id >= 1005))
      }
  
      @Test
      void apply() {
          List<UserInfo> userInfos = userInfoService.lambdaQuery()
                  .apply("id < {0}", 20)
                  .list();
          log.info("userInfo:{}", userInfos);
          // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id < 20)
      }
  
      @Test
      void last() {
          List<UserInfo> userInfos = userInfoService.lambdaQuery()
                  .last("limit 1")
                  .list();
          log.info("userInfo:{}", userInfos);
          // 等价sql：SELECT id,user_name,age,source FROM user_info limit 1
      }
  }
  ```

  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128222802298.png)

有了这些`API`和`条件构造器`，是不是一行数据库操作的代码都没有写，基础的CURD统统都能搞定了；

但是，实际的业务并不只是基础的CURD，有没有发现，联表查询MyBatis Plus并没有支持，但是关联查询在业务开发中，又会经常用到，如果单纯基于MyBatis Plus，要实现联表，就只能自己写配置，写SQL去实现了，这就违背了本文的初衷了；

那有没有一款框架能帮助我们去封装联表查询呢？那就是下面要介绍的一款框架`MyBatis Plus Join`



## MyBatis Plus Join

`MyBatis Plus Join`一款专门解决MyBatis Plus 关联查询问题的扩展框架，他并不一款全新的框架，而是基于`MyBatis Plus`功能的增强，所以`MyBatis Plus`的所有功能`MyBatis Plus Join`同样拥有；框架的使用方式和`MyBatis Plus`一样简单，几行代码就能实现联表查询的功能

> 官方仓库：https://gitee.com/best_handsome/mybatis-plus-join

### 准备工作

为了方便做联表测试，这里预先准备三张表（学校表、班级表、学生表），用来做关联查询，sql如下：

```sql
DROP TABLE IF EXISTS `school_info`;
CREATE TABLE `school_info`  (
  `id` int(11) NOT NULL,
  `school_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学校名称',
  `school_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学校地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `school_info` VALUES (1, 'XXX小学', 'xx区xx街道80号');

-- ----------------------------------------------------------------

CREATE TABLE `class_info`  (
  `id` int(11) NOT NULL,
  `class_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `class_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `school_id` int(11) NOT NULL COMMENT '隶属的学校',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `class_info` VALUES (1, '一年级1班', NULL, 1);
INSERT INTO `class_info` VALUES (2, '一年级2班', NULL, 1);

-- ----------------------------------------------------------------

CREATE TABLE `student_info`  (
  `id` int(11) NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `class_id` int(11) NULL DEFAULT NULL,
  `school_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `student_info` VALUES (1, '张三', 7, 1, 1);
INSERT INTO `student_info` VALUES (2, '李四', 7, 2, 1);
INSERT INTO `student_info` VALUES (3, '王五', 8, 1, 1);
INSERT INTO `student_info` VALUES (4, '赵六', 8, 1, 1);
```



### 基础MyBatis Plus代码生成

参考MyBatisX的插件使用，这里就不重复了

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128224524125.png)



### MyBatis Plus Join 核心类说明

- MPJBaseMapper

  扩展了MyBatis Plus的 `BaseMapper` 接口

  ```java
  public interface MPJBaseMapper<T> extends BaseMapper<T> {
      Integer selectJoinCount(@Param("ew") MPJBaseJoin var1);
  
      <DTO> DTO selectJoinOne(@Param("resultTypeClass_Eg1sG") Class<DTO> var1, @Param("ew") MPJBaseJoin var2);
  
      Map<String, Object> selectJoinMap(@Param("ew") MPJBaseJoin var1);
  
      <DTO> List<DTO> selectJoinList(@Param("resultTypeClass_Eg1sG") Class<DTO> var1, @Param("ew") MPJBaseJoin var2);
  
      List<Map<String, Object>> selectJoinMaps(@Param("ew") MPJBaseJoin var1);
  
      <DTO, P extends IPage<?>> IPage<DTO> selectJoinPage(P var1, @Param("resultTypeClass_Eg1sG") Class<DTO> var2, @Param("ew") MPJBaseJoin var3);
  
      <P extends IPage<?>> IPage<Map<String, Object>> selectJoinMapsPage(P var1, @Param("ew") MPJBaseJoin var2);
  }
  ```

- MPJBaseService 

  扩展了MyBatis Plus的 `IService` 接口

  ```java
  public interface MPJBaseService<T> extends IService<T> {
      Integer selectJoinCount(MPJBaseJoin var1);
  
      <DTO> DTO selectJoinOne(Class<DTO> var1, MPJBaseJoin var2);
  
      <DTO> List<DTO> selectJoinList(Class<DTO> var1, MPJBaseJoin var2);
  
      <DTO, P extends IPage<?>> IPage<DTO> selectJoinListPage(P var1, Class<DTO> var2, MPJBaseJoin var3);
  
      Map<String, Object> selectJoinMap(MPJBaseJoin var1);
  
      List<Map<String, Object>> selectJoinMaps(MPJBaseJoin var1);
  
      <P extends IPage<Map<String, Object>>> IPage<Map<String, Object>> selectJoinMapsPage(P var1, MPJBaseJoin var2);
  }
  ```

- MPJBaseServiceImpl 

  扩展了MyBatis Plus的 `ServiceImpl` 接口实现

  ```java
  public class MPJBaseServiceImpl<M extends MPJBaseMapper<T>, T> extends ServiceImpl<M, T> implements MPJBaseService<T> {
  ...
  }
  ```

  

###  基础代码调整

简单的三处调整，就能完成整合工作

- 将mapper改为继承MPJBaseMapper (必选)

  修改前

  ```java
  public interface StudentInfoMapper extends BaseMapper<StudentInfo> {
  }
  ```

  **修改后**

  ```java
  public interface StudentInfoMapper extends MPJBaseMapper<StudentInfo> {
  }
  ```

- 将service改为继承MPJBaseService (可选)

  修改前

  ```java
  public interface StudentInfoService extends BaseService<StudentInfo> {
  }
  ```

  **修改后**

  ```java
  public interface StudentInfoService extends MPJBaseService<StudentInfo> {
  }
  ```

- 将serviceImpl改为继承MPJBaseServiceImpl (可选)

  修改前

  ```java
  @Service
  public class StudentInfoServiceImpl extends BaseServiceImpl<StudentInfoMapper, StudentInfo>
      implements StudentInfoService{
  }
  ```

  **修改后**

  ```java
  @Service
  public class StudentInfoServiceImpl extends MPJBaseServiceImpl<StudentInfoMapper, StudentInfo>
      implements StudentInfoService{
  }
  ```



### 联表测试

测试需求：查询学生所处的班级及学校

#### DTO定义

用于联表查询后接收数据的实体类

```java
@Data
public class StudentInfoDTO {
	// 学生id
    private Integer id;

    // 性名
    private String name;

    // 年龄
    private Integer age;

    // 班级名称
    private String className;

    // 学校名称
    private String schoolName;

    // 学校地址 用于测试别名
    private String scAddr;
}
```

#### 单记录联表查询

```java
@Autowired
StudentInfoService sutdentInfoService;

/**
 * 联表查询单个
 */
@Test
public void selectJoinOne() {
    StudentInfoDTO studentInfoDTO = sutdentInfoService.selectJoinOne(StudentInfoDTO.class,
            new MPJLambdaWrapper<StudentInfo>()
                    .selectAll(StudentInfo.class)
                    .select(SchoolInfo::getSchoolName)
                    .selectAs(SchoolInfo::getSchoolAddr, StudentInfoDTO::getScAddr)
                    .select(ClassInfo::getClassName)
                    .leftJoin(SchoolInfo.class, SchoolInfo::getId, StudentInfo::getSchoolId)
                    .leftJoin(ClassInfo.class, ClassInfo::getId, StudentInfo::getClassId)
                    .eq(StudentInfo::getId, 1));
    log.info("selectJoinOne:{}", JSON.toJSONString(studentInfoDTO));
}
```

**简单说明**

- StudentInfoDTO.class

  表示resultType，用于接收联表查询之后的数据库返回

- selectAll

  指明查询实体对应的所有字段

- select

  指定查询列，同一个select只能指明单个表的列，所以多表关联时需要使用多个select去指明不同表的列

- selectAs

  重命名，表现在sql层面是会给字段加上`as`（别名）；主要用在*数据库字段名也实体对象的名称不一致*的情况；

- leftJoin、rightJoin、innerJoin

  左链接、右连接、等值连接；不懂这三种连接方式的，可参考：[SQL中 inner join、left join、right join、full join 到底怎么选？详解来了](https://mp.weixin.qq.com/s/212kvP0Osjljgkj3BSJ8Nw)

  - 参数一：参与联表的对象
  - 参数二：on关联的指定，此属性必须是第一个对象中的值
  - 参数三：参与连表的ON的另一个实体类属性

- 条件构造器

  联表后可能会存在各种筛选条件，可以根据上面对条件构造器的介绍，指明所需要的筛选条件，比如上面`.eq(StudentInfo::getId, 1))`，就是用来指明ID为1的学生信息。
  
- 表名

  默认主表别名是`t`，其他的表别名以先后调用的顺序使用*t1,t2,t3....*；

  需要直接apply语句的时候，就得知道对应的表面是什么再进行添加，所以不到万不得已的时候，不建议直接追加语句。

**等价SQL**

```sql
SELECT 
	t.id,
	t.name,
	t.age,
	t.class_id,
	t.school_id,
	t1.school_name,
	t1.school_addr AS scAddr,
	t2.class_name
FROM 
	student_info t
	LEFT JOIN school_info t1 ON (t1.id = t.school_id)
	LEFT JOIN class_info t2 ON (t2.id = t.class_id)
WHERE (t.id = ?)
```

执行结果

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128230800915.png)



#### 联表查多条

```java
@Autowired
StudentInfoService sutdentInfoService;

/**
 * 联表查询批量
 */
@Test
public void selectJoinList() {
    List<StudentInfoDTO> studentInfoDTOS = sutdentInfoService.selectJoinList(StudentInfoDTO.class,
            new MPJLambdaWrapper<StudentInfo>()
                    .selectAll(StudentInfo.class)
                    .select(SchoolInfo::getSchoolName)
                    .selectAs(SchoolInfo::getSchoolAddr, StudentInfoDTO::getScAddr)
                    .select(ClassInfo::getClassName)
                    .leftJoin(SchoolInfo.class, SchoolInfo::getId, StudentInfo::getSchoolId)
                    .leftJoin(ClassInfo.class, ClassInfo::getId, StudentInfo::getClassId)
            //.eq(StudentInfo::getId, 1)
    );
    log.info("selectJoinList:{}", JSON.toJSONString(studentInfoDTOS));
}
```

等价SQL

```sql
SELECT 
	t.id,
	t.name,
	t.age,
	t.class_id,
	t.school_id,
	t1.school_name,
	t1.school_addr AS scAddr,
	t2.class_name
FROM 
	student_info t
	LEFT JOIN school_info t1 ON (t1.id = t.school_id)
	LEFT JOIN class_info t2 ON (t2.id = t.class_id)
```

执行结果

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128230958814.png)



#### 联表分页查询

```java
@Autowired
StudentInfoService sutdentInfoService;

/**
 * 分页查询
 */
@Test
public void selectJoinPage() {
    IPage<StudentInfoDTO> studentInfoDTOIPage = sutdentInfoService.selectJoinListPage(new Page<>(1, 2), StudentInfoDTO.class,
            new MPJLambdaWrapper<StudentInfo>()
                    .selectAll(StudentInfo.class)
                    .select(SchoolInfo::getSchoolName)
                    .selectAs(SchoolInfo::getSchoolAddr, StudentInfoDTO::getScAddr)
                    .select(ClassInfo::getClassName)
                    .leftJoin(SchoolInfo.class, SchoolInfo::getId, StudentInfo::getSchoolId)
                    .leftJoin(ClassInfo.class, ClassInfo::getId, StudentInfo::getClassId)
                    .orderByAsc(StudentInfo::getId)
    );
    log.info("selectJoinPage:{}", JSON.toJSONString(studentInfoDTOIPage));
}
```

等价SQL

```sql
SELECT 
	t.id,
	t.name,
	t.age,
	t.class_id,
	t.school_id,
	t1.school_name,
	t1.school_addr AS scAddr,
	t2.class_name
FROM 
	student_info t
	LEFT JOIN school_info t1 ON (t1.id = t.school_id)
	LEFT JOIN class_info t2 ON (t2.id = t.class_id)
ORDER BY 
	t.id ASC 
LIMIT 2
```

执行结果

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20211128231137223.png)



## 总结

好了，`MyBatis Plus` + `MyBatisX` + `MyBatis Plus Join`的详细使用教程就讲解完了；再回头看，是不是发现业务功能开发一下子变的简单多了；

本文也只是介绍了大部分常用的内容，并没有列举出两款框架的所有东西；知道怎么使用之后，更多的使用细节，可以结合API文档以及各种条件构造器，灵活变通，即可完成各种想要的效果；

码字不易，如果觉得好用，帮忙点个赞，点个再看呗！感激涕零...



---

最近在整理SpringBoot的学习教程，如果你也在学习SpringBoot，可以[点击查看](https://github.com/vehang/ehang-spring-boot)，欢迎交流！欢迎`star`

> 本文示例教程：https://github.com/vehang/ehang-spring-boot/tree/main/spring-boot-010-mysql-mybatis-plus
>
> SpringBoot教程：https://github.com/vehang/ehang-spring-boot

## 联系我

| 公众号                                                       | 微信                                                         | 技术交流群                                                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/gzh_px_2022-03-21+23_04_24.png) | ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/wechat_px_2022-03-21+23_05_51.jpeg) | ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/group_px_2022-03-21+23_07_35.jpeg) |
