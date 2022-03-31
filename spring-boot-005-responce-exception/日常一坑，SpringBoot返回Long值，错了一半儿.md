![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220329221904418.png)

大家好，我是一航！

昨天下午，有伙伴儿在群里面问了下面这么个问题：

> 数据库用bigint存储了记录的ID，Java代码用Long类型映射记录对应ID的值；前端调用SpringBoot的接口获取数据，后端debug时Long型ID能正常取到值，但是返回到前端之后，ID后半段被“偷”了，查询id为1、2、3的时候又是正常的；数据如下：![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220329184554320.png)

两值对比：

```
后端的值：1508733541883731970
前端的值：1508733541883732000
```

很明显，出**精度问题了**，但是不了解细节的时候，很容易一脸懵



为什么会这样呢？

## 原因

这是因为Javascript中数字的精度是有限的，Java中的Long精度超出了Javascript的处理范围。JS 遵循 IEEE 754 规范，采用双精度存储（double precision），占用 64 bit。其结构如图：

- 1位（s） 用来表示符号位
- 11位（e） 用来表示指数
- 52位（f） 表示尾数

尾数位最大是 52 位，因此 JS 中能精准表示的最大整数是 Math.pow(2, 53)，十进制即 `9007199254740992`，任何大于9007199254740992都会出现精度丢失的问题；

为了验证，我们在浏览器中按`F12`，在Console中做如下测试：

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220329185614415.png)

结果和我们上面所想的一样。



## 解决方案

但实际开发过程中，数据库的bigint，Java的Long都是比较常用的数据类型，不可能因为前端JS的精度问题，而不去使用，因此，为了避免精度丢失，针对这种比较大的数值类型，可以将其以文本的形式返回；

![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220329222726931.png)

SpringBoot的对象序列化默认采用的是`Jackson`，有以下三种方式来将数值类型转换为文本串

测试使用的接口及对象：

```java
@GetMapping("/user")
public User getUser(){
    User user = new User();
    user.setId(1508733541883731970L);
    user.setAge(10);
    user.setName("zhangsan");
    user.setGender((short) 1);
    return user;
}

@Data
class User{
    Long id;
    String name;
    Integer age;
    Short gender;
}
```

### 方式一：属性序列化注解@JsonSerialize

可以将对象中的指定属性以文本的方式进行序列化

```java
@Data
class User{
    @JsonSerialize(using = ToStringSerializer.class)
    Long id;
    String name;
    @JsonSerialize(using = ToStringSerializer.class)
    Integer age;
    Short gender;
}
```

测试数据：

```json
{
  "id": "1508733541883731970",
  "name": "zhangsan",
  "age": "10",
  "gender": 1
}
```

配置了`@JsonSerialize`的`id`和`age`被转换成了文本

- 优点

  灵活，针对对象的属性配置，想转那个就转那个，不会干扰到其他属性或者对象

- 缺点

  每个需要转换的属性都需要配置，有点苦力活的意思



### 方式二：全局配置，将数值类型转换为文本

如果需要将所有的number类型的全部转换成文本，可以在application.yml中添加如下配置：

```yaml
spring:
  jackson:
    generator:
      write_numbers_as_strings: true #序列化的时候，将数值类型全部转换成字符串返回
```

测试示例：

```json
{
  "id": "1508733541883731970",
  "name": "zhangsan",
  "age": "10",
  "gender": "1"
}
```

- 优点：

  配置完，所有的数值类型全部转换为文本，一劳永逸；

- 缺点

  上面的优点，也是缺点的一部分，过于笼统，不够灵活；



### 方式三，单类型转换

可以自定义一个Jackson对象转换构造器，将指定类型以指定的序列化方式进行转换，比如遇到`Long`、`Double`的时候，才转换为文本

```java
@Bean("jackson2ObjectMapperBuilderCustomizer")
public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    Jackson2ObjectMapperBuilderCustomizer customizer = new Jackson2ObjectMapperBuilderCustomizer() {
        @Override
        public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance)
                    .serializerByType(Long.TYPE, ToStringSerializer.instance);
        }
    };
    return customizer;
}
```

测试数据：

```json
{
  "id": "1508733541883731970",
  "name": "zhangsan",
  "age": 10,
  "gender": 1
}
```

发现之后为Long类型的id转换成了文本；`Integer`和`Short`类型的并未受到影响；

三种方式都有各自适用的场景，相比而言，第一种和第三种用的相对普遍一点，可以根据自己的实际情况酌情选择；

好了！如果你是Java从业者、或者正在学习Java，**欢迎扫文末的二维码加入技术交流群**；互相学习，一起进步。







