package com.ehang.responce.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ehang
 * @title: UserRequestDto
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/6 22:18
 */
@Data
public class UserRequestDto {
    /**
     * id
     * <p>
     */
    @NotNull(message = "ID不能为空", groups = {UpdateValidate.class})
    private Integer id;

    /**
     * 用户名
     * <p>
     * groups 可以指定多个 可以在不同的分组场景下都去校验
     */
    @NotBlank(message = "用户名不能为空", groups = {AddValidate.class, UpdateValidate.class})
    private String userName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = AddValidate.class)
    private String passWord;

    /**
     * 添加的校验分组
     */
    public interface AddValidate {
    }

    /**
     * 修改的校验分组
     */
    public interface UpdateValidate extends AddValidate {
    }
}
