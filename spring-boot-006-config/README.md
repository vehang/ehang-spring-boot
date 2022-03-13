SpringBoot配置文件的主要目的是为了方便程序员去修改默认的配置项，比如数据库的`地址`、`用户名`、`密码`等个性化的配置；除此之外，有时候也需要自定义一些个性化的配置项来满足业务的需要；如何配置？如何获取？

`@Value`注解除了获取单个配置，SpEL表达式如何进行复杂的处理？，下面就一起来玩一下；

## 目录

> 本文的主要内容是想说说@Value对SpEL表达式的支持，考虑到一些对自定义配置、获取不太了解的朋友，这里就从头来聊一下，如果基础的部分您已经了解，只想了解SpEL表达式的部分，可以直接跳到最后一小节；

- 配置文件
  - 方式
  - 支持的类型
- 配置获取
  - @ConfigurationProperties
  - @Value
    - 单值获取
    - SpEL表达式

## 配置文件

### 方式

当通过脚手架创建一个SpringBoot项目时，会自动在`src/main/resources`目录下创建一个默认的`application.properties`配置文件，文件支持`properties`和`yaml`两种方式
#### properties（默认）

properties格式的配置文件是Java项目中运用最广泛的一种方式，也是SpringBoot的默认方式，配置采用`key=value`方式，对于单个配置项，此结构非常清晰明了；
- properties的特点
 - 语法：`key=value`
 - `=`等号后面，值前面的`空格`，会自动忽略掉;值后面的空格，不会忽略;
   
   > 这是一个非常值得注意的点，很多时候，无意间的空格，可能导致配置项错误，但是编辑器无法直观的看出，导致问题排查非常困难；
 - `=`等号后面""不会被忽略
 - `#`后面的内容为注释，不生效

#### yml

YAML (YAML Ain't a Markup Language)YAML不是一种标记语言，通常以.yml为后缀的文件，是一种直观的能够被电脑识别的数据序列化格式，并且容易被人类阅读，容易和脚本语言交互的，可以被支持YAML库的不同的编程语言程序导入，一种专门用来写配置文件的语言。如果你想使用yml格式，可以直接将`application.properties`重命名为`application.yml`即可

- YAML的优点
  - YAML易于人们阅读。
  - YAML数据在编程语言之间是可移植的。
  - YAML匹配敏捷语言的本机数据结构。
  - YAML具有一致的模型来支持通用工具。
  - YAML支持单程处理。
  - YAML具有表现力和可扩展性。
  - YAML易于实现和使用。

- 语法约定
  - k: v
  
    用于描述键值对关系，冒好后面必须有一个空格
  
  - 使用空格的缩进表示层级关系
  
  - 大小写敏感
  
  - 缩进只允许使用空格，不允许使用tab
  
  - 松散表示；java中的驼峰，在yml中通过-表示，如：java的属性helloWord，yml通过`hello-word`与之映射

### 支持的类型
配置文件支持`基础数据类型`、`文本`、`日期`、`对象`、`List`、`Map`
- 文本
  - yml

    ```yml
    user-info:
      name: zhangsan
    ```
  - properties

    ```properties
    user-info.name=zhangsan
    ```
  
- 整数

  - yml

    ```yml
    user-info:
      age: 50
    ```
  - properties
    ```properties
    user-info.age=50
    ```

- 布尔
  - yml

    ```yml
    user-info:
      marital: true
    ```
  - properties

    ```properties
    user-info.marital=true
    ```
    
  
- 浮点数
  - yml

    ```yml
    user-info:
      weight: 68.5
    ```
  - properties

    ```properties
    user-info.weight=68.5
    ```

- 科学计数法
  - yml

    ```yml
    user-info:
      xxxx: 1E4
    ```
  - properties

    ```properties
    user-info.xxxx=1E4
    ```

- 日期
  - yml

    ```yml
    user-info:
      birth: 1970/10/02
    ```
  - properties

    ```properties
    user-info.birth=1970/10/02
    ```
    注意格式`yyyy/MM/dd`

- 对象
  - yml

    ```yml
    user-info:
      obj:
        key1: va1
        key2: va2
    ```
  - properties

    ```properties
    user-info.obj.key1=va1
    user-info.obj.key2=va2
    ```

- 数组
  - yml

    ```yml
    user-info:
      hobby:
        - smoke
        - drink
        - perm
    ```
  - properties

    ```properties
    user-info.hobby[0]=smoke
    user-info.hobby[1]=drink
    user-info.hobby[2]=perm
    ```

- 集合
  - yml

    ```yml
    user-info:
      maps:
          key1:
            ck1: cv1
            ck2: 2
          key2: va2
    ```
  - properties

    ```properties
    user-info.maps.key1.ck1=cv1
    user-info.maps.key1.ck2=2
    user-info.maps.key2=va2
    ```
    

- 以下是同一份配置，两种格式的对比示例：
  
  ![](https://cdn.jsdelivr.net/gh/mbb2100/picgo_imgs/image-20220308234115397.png)

## 配置获取

目前SpringBoot中获取配置文件的方式有多种，最常用的就是通过`@ConfigurationProperties`或者`@Value`注入

- 差异

  @Value获取值和@ConfigurationProperties获取值比较
  
  | 特点                 | @ConfigurationProperties | @Value     |
  | -------------------- | ------------------------ | ---------- |
  | 功能                 | 批量注入配置文件中的属性 | 一个个指定 |
  | 松散绑定（松散语法） | **支持**                 | 不支持     |
  | SpEL                 | 不支持                   | **支持**   |
  | JSR303数据校验       | **支持**                 | 不支持     |
  | 复杂类型封装         | **支持**                 | 不支持     |

### @ConfigurationProperties

将配置文件中的属性批量注入到对象中，常用多个配置项、或者复杂的属性结构，比如上面的yml配置文件示例，我就可以通过下面的对象结合`@ConfigurationProperties`进行注入

```java
@Data
@Component
@ConfigurationProperties(prefix = "user-info")
public class UserConfigByCP {
    private String name;

    private Integer age;

    private Date birth;

    private String email;

    private ObjConfig obj;

    private String notFound;

    private List<String> hobby;

    private List<PetConfig> pets;

    private Map<String, Object> maps;
}
```

PetConfig对象：

```java
@Data
public class PetConfig {
    private String name;

    private String breed;
}
```

ObjConfig对象：

```java
@Data
public class ObjConfig {
    private String key1;

    private String key2;
}
```

- prefix

  用于指明配置文件的前缀，指明之后，对象将只会注入前缀下的所有属性；

- @Data
  Lombok插件的注解，此处主要用于对象生成getter、setter方法；
  
  

### @Value

@Value一般用于单个配置项的获取，因为其支持`SpEL`表达式，所以除了获取配置属性之外，还可以对值进行更多的特殊处理；

#### 单值获取

开发中，最常用的一种方式`@Value("${}")`，将单个配置项注入到对象中，方便业务代码中进行使用，不过此方式不支持配置文件的复杂结构，只适合一些基础的数据类型的注入：

```java
@Component
@Data
public class UserConfigByValue {
    /**
     * 相当于固定值的串”1+1“
     */
    @Value("1+1")
    private String constant;

    @Value("${user-info.name}")
    private String name;

    @Value("${user-info.age}")
    private Integer age;

    @Value("${user-info.birth}")
    private Date birth;

    /**
     * 当使用@Value时，如果获取的配置可能出现不配置的情况，需要通过: 在右侧指定一个默认值，否则启动会报：BeanCreationException 异常
     * 指定默认值之后，就能正常启动了
     */
    @Value("${user-info.notfound:}")
    private String notfound;

//    @Value 不支持配置文件的复杂结构
}
```

注意事项：

- 类型必须匹配

  比如`abc`就只能通过`String`来接受，否则会因为注入失败导致项目启动失败；

- 配置项可能不存在时，需要设置默认值

  当`user-info.notfound`在配置文件中并不存在，但在代码中又通过`@Value("${user-info.notfound}")`；如果不设置默认值的话，就会因为注入失败导致项目无法启动；因此我们需要使用`:`来为其设置一个默认值（:右边就是指定的默认值），当配置项不存在时，就将指定的默认值赋给属性；如上示例：`@Value("${user-info.notfound:}")`,当配置项不存在时，就会将一个空串""赋给`notfound`属性；

- 无法加载复杂的结构

  通过`${}`注入复杂的结构，如`List`等；如果需要处理复杂结构时，可以使用`#{}`或者`@ConfigurationProperties`注入；

#### SpEL表达式

@Value除了基本的配置项获取之外，还支持`SpEL表达式`进行进行更多的复杂操作，比如`数学运算`、`比较`、`三元表达式（if-elas）`、`对象方式，属性调用`、`正则表达式`、`系统环境变量获取`等操作。

##### SpEL字面量

直接给属性指定具体的值，此方式除了`List`、`Map`这种偶尔使用一下，基础数据类型在实际的开发中用的非常少；

- **整数**：#{8}
- **小数**：#{8.8}
- **科学计数法**：#{1e4}
- **String**：可以使用单引号或者双引号作为字符串的定界符号。
- **Boolean**：#{true}
- **List**：#{{'a','b','c'}}
- **Map**：#{{k1: 'v1', k2: 'v2'}}

示例代码：

```java
@Value("#{1}")
private Integer integer;  // 1

@Value("#{1.1}")
private Float aFloat;  // 1.1

@Value("#{1e4}")
private Double aDouble;  // 10000.0

@Value("#{'张三'}")
private String string;  // 张三

@Value("#{true}")
private Boolean aBoolean; // true

@Value("#{{'a','b','c'}}")
private List<String> list;  // ["a","b","c"]

@Value("#{{k1: 'v1', k2: 'v2'}}")
private Map map;   // {"k1":"v1","k2":"v2"}
```

##### SpEL引用bean , 属性和方法:

通过SpEL表达式注入Spring容器中的对象，调用对象的方法得到返回值，引用对象中的属性；方法的调用个人认为`需要适度使用，复杂之后，只会让代码的可读性降低，维护难度提高`；

- 引用其他对象:`#{uuidUtil}`

  示例对象

  ```java
  @Component
  public class UuidUtil {
  
      public String getUuid() {
          return UUID.randomUUID().toString();
      }
  }
  ```

  对象引用

  ```java
  /**
   * 引用Spring容器中的其他对象，#{}中指定的是beanName
   */
  @Value("#{uuidUtil}")
  private UuidUtil uuidUtil;
  ```

- 引用其他对象的属性、方法：`#{uuidUtil.getUuid}`

  ```java
  /**
   * 容器内对象的方法调用
   * 将UuidUtil的getUuid()的返回值作为uuid的属性值
   */
  @Value("#{uuidUtil.getUuid}")
  private String uuid;  // dcce9b8-328c-4bef-9595-4cf8b0260ce8
  ```

- 链式操作：`#{uuidUtil.getUuid.replace('-','')}`

  ```java
  /**
   * 链式调用
   */
  @Value("#{uuidUtil.getUuid.replace('-','')}")
  private String shortUuid;  // b0e144f874264af8b1d8deb3093f6ffe
  ```

- 调用静态方法静态属性；如：`#{T(java.lang.Math).PI}`

  静态工具类：

  ```java
  public class RandomUtil {
      // 产生随机数
      public static Integer num() {
          Random random = new Random();
          return random.nextInt(10000);
      }
  
      // 用户名后面追加随机数
      public static String nickName(String name) {
          Random random = new Random();
          return name + "_" + random.nextInt(10000);
      }
  }
  ```
  
  @Value调用静态方法示例
  
  ```java
  /**
   * 静态方法的调用
   */
  @Value("#{T(com.ehang.config.value.spel.RandomUtil).num()}")
  private Integer random;  // 随机数
  
  /**
   * #{} 与 ${} 的结合使用
   */
  @Value("#{T(com.ehang.config.value.spel.RandomUtil).nickName('${user-info.name}')}")
  private String nickName;  // zhangsan_随机数
  ```

##### SpEL支持的运算符号

- 算术运算符：+，-，*，/，%，^(加号还可以用作字符串连接)

  ```java
  /**
   * 算术运算符
   * +(可做字符串连接)，-，*，/，%，^
   */
  @Value("#{1+1}")
  private Integer arithmetic;  // 2
  ```

- 比较运算符：< , > , == , >= , <= , lt , gt , eg , le , ge

  ```java
  /**
   * <  , >  , == , >= , <= ,
   * lt , gt , eg , le , ge
   */
  @Value("#{1 lt 2}")
  //@Value("#{1 < 2}")
  private Boolean compare; // true
  ```

- 逻辑运算符：and , or , not 

  ```java
  /**
   * 逻辑运算符
   * 与、 或、 非
   * and or  not
   * &&  ||  !
   */
  @Value("#{!(1 > 2)}")
  //@Value("#{(1>2) or (4>3)}")
  //@Value("#{(2>1) and (4>3)}")
  private Boolean Logical;   // true
  ```

- if-else (三元表达式)：`[条件]？[结果1]:[结果2]`

  ```java
  /**
   * 三目表达式
   */
  @Value("#{(1>2) ? 'a':'b'}")
  private String ifElse;  // b
  ```

- 正则表达式：`#{'1' matches '\\d+' }`

  ```java
  /**
   * 正则表达式
   */
  @Value("#{'1' matches '\\d+' }")
  private Boolean matches;   // true
  ```

##### SpEL获取系统环境变量

- 获取所有支持的环境变量

  ```java
  public static void main(String[] args) {
      Properties properties = System.getProperties();
      Iterator it =  properties.entrySet().iterator();
      while(it.hasNext())
      {
          Map.Entry entry = (Map.Entry)it.next();
          System.out.print(entry.getKey()+"=");
          System.out.println(entry.getValue());
      }
  }
  ```

  以下是我本机的环境变量：

  ```properties
  sun.boot.library.path=D:\xxx\jre\bin
  java.vm.version=25.261-b12
  java.vm.vendor=Oracle Corporation
  java.vendor.url=http://java.oracle.com/
  path.separator=;
  java.vm.name=Java HotSpot(TM) Client VM
  file.encoding.pkg=sun.io
  user.country=CN
  user.script=
  sun.java.launcher=SUN_STANDARD
  sun.os.patch.level=
  java.vm.specification.name=Java Virtual Machine Specification
  user.dir=F:\web\ehang-spring-boot
  java.runtime.version=1.8.0_261-b12
  java.awt.graphicsenv=sun.awt.Win32GraphicsEnvironment
  java.endorsed.dirs=D:\xxx\jre\lib\endorsed
  os.arch=x86
  java.io.tmpdir=C:\Users\LENOVO\AppData\Local\Temp\
  line.separator=
  
  java.vm.specification.vendor=Oracle Corporation
  user.variant=
  os.name=Windows 10
  sun.jnu.encoding=GBK
  java.library.path=D:\xxx\bin
  java.specification.name=Java Platform API Specification
  java.class.version=52.0
  sun.management.compiler=HotSpot Client Compiler
  os.version=10.0
  user.home=C:\Users\LENOVO
  user.timezone=
  java.awt.printerjob=sun.awt.windows.WPrinterJob
  file.encoding=UTF-8
  java.specification.version=1.8
  java.class.path=xxx
  user.name=LENOVO
  java.vm.specification.version=1.8
  sun.java.command=com.ehang.config.value.spel.RandomUtil
  java.home=D:\xxx\jre
  sun.arch.data.model=32
  user.language=zh
  java.specification.vendor=Oracle Corporation
  awt.toolkit=sun.awt.windows.WToolkit
  java.vm.info=mixed mode, sharing
  java.version=1.8.0_261
  java.ext.dirs=D:\xxx\ext
  java.vendor=Oracle Corporation
  file.separator=\
  java.vendor.url.bug=http://bugreport.sun.com/bugreport/
  sun.io.unicode.encoding=UnicodeLittle
  sun.cpu.endian=little
  sun.desktop=windows
  sun.cpu.isalist=pentium_pro+mmx pentium_pro pentium+mmx pentium i486 i386 i86
  ```

- @Value获取环境变量

  ```java
  /**
   * 读取环境变量
   */
  @Value("#{systemProperties['java.home']}")
  private String javaHome;  // java的安装路径 D:\xxx\jre
  ```


