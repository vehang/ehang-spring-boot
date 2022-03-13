package com.ehang.config.value.spel;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author ehang
 * @title: UserConfigByValueSpEL
 * @projectName ehang-spring-boot
 * @description: TODO 通过SpEL对配置文件进行更复杂的操作
 * @date 2022/3/8 15:57
 */
@Component
@Data
public class UserConfigByValueSpEL {
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

    /**
     * 算术运算符
     * +(可做字符串连接)，-，*，/，%，^
     */
    @Value("#{1+1}")
    private Integer arithmetic;  // 2

    /**
     * <  , >  , == , >= , <= ,
     * lt , gt , eg , le , ge
     */
    @Value("#{1 lt 2}")
    //@Value("#{1 < 2}")
    private Boolean compare; // true

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

    /**
     * 三目表达式
     */
    @Value("#{(1>2) ? 'a':'b'}")
    private String ifElse;  // b

    /**
     * 正则表达式
     */
    @Value("#{'1' matches '\\d+' }")
    private Boolean matches;   // true

    /**
     * 读取环境变量
     */
    @Value("#{systemProperties['java.home']}")
    private String javaHome;  // java的安装路径

    /**
     * 引用Spring容器中的其他对象，#{}中指定的是beanName
     */
    @Value("#{uuidUtil}")
    private UuidUtil uuidUtil;

    /**
     * 容器内对象的方法调用
     * 将UuidUtil的getUuid()的返回值作为uuid的属性值
     */
    @Value("#{uuidUtil.getUuid}")
    private String uuid;  // dcce9b8-328c-4bef-9595-4cf8b0260ce8

    /**
     * 链式调用
     */
    @Value("#{uuidUtil.getUuid.replace('-','')}")
    private String shortUuid;  // b0e144f874264af8b1d8deb3093f6ffe

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
}
