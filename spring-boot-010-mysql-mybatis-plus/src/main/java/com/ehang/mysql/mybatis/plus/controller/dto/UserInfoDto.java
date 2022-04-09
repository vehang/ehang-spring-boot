package com.ehang.mysql.mybatis.plus.controller.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author ehang
 * @title: UserInfo
 * @projectName spring-boot-010-mysql-mybatis-plus
 * @description: TODO
 * @date 2021/12/7 18:48
 */
@Data
public class UserInfoDto {
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @Min(value = 0,message = "年龄不能小于0岁")
    @Max(value = 120,message = "年龄不能大于120岁")
    private Integer age;

    /**
     * 邮箱
     */
    private String email;
}
