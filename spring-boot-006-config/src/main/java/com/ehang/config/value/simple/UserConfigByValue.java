package com.ehang.config.value.simple;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ehang
 * @title: UserConfig
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/8 15:57
 */
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
//
//    @Value("${user-info.hobby}")
//    private List<String> hobby;
//
//    @Value("${user-info.pets}")
//    private List<PetConfig> pets;
//
//    @Value("${user-info.maps}")
//    private Map<String, Object> maps;
}
