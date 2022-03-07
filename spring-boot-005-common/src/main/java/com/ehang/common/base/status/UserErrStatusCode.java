package com.ehang.common.base.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ehang
 * @title: UserErrStatusCode
 * @projectName ehang-spring-boot
 * @description: TODO 用户相关的错误码 目的是为了加强错误码区分
 * @date 2022/3/6 16:28
 */
@Getter
@AllArgsConstructor
public enum UserErrStatusCode implements IStatusCode {
    ERR_2000(2000, "用户信息不存在"),

    ERR_2001(2001, "用户昵称格式错误");
    /**
     * 状态码
     */
    private Integer status;

    /**
     * 状态码描述
     */
    private String msg;
}
