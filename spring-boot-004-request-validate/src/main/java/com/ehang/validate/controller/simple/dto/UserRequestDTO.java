package com.ehang.validate.controller.simple.dto;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author ehang
 * @title: UserRequestDTO
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/4 12:51
 */
@Data
public class UserRequestDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "姓名不能为空")
    public String userName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    public String passWord;

    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于0岁")
    @Max(value = 120, message = "年龄不能大于120岁")
    private Integer age;

    /**
     * 手机号码；使用正则进行匹配
     */
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$", message = "号码格式不正确!")
    private String phoneNum;
}
