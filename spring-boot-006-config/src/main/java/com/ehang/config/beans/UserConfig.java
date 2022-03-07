package com.ehang.config.beans;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ehang
 * @title: UserConfig
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/7 23:07
 */
@Data
@Component
@ConfigurationProperties(prefix = "user")
public class UserConfig {
    private String name;

    private Integer age;

    private Date birth;

    private List<String> hobby;

    private List<PetConfig> pets;

    private Map<String, Object> maps;
}
