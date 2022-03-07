package com.ehang.common.base.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ehang
 * @title: BaseStatusCode
 * @projectName ehang-spring-boot
 * @description: TODO 基础的状态码
 * @date 2022/3/6 16:18
 */
@Getter
@AllArgsConstructor
public enum BaseStatusCode implements IStatusCode {
    SUCCESS(200, "成功!"),

    ERR_1000(1000, "参数错误!"),

    ERR_9999(9999, "未知错误!");

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 状态码描述
     */
    private String msg;
}
