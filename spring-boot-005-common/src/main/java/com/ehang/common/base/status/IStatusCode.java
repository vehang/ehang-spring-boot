package com.ehang.common.base.status;

/**
 * @author ehang
 * @title: IStatusCode
 * @projectName ehang-spring-boot
 * @description: TODO 错误码的接口
 * @date 2022/3/6 16:16
 */
public interface IStatusCode {
    /**
     * 获取状态码
     *
     * @return 返回
     */
    Integer getStatus();

    /**
     * 获取状态描述
     *
     * @return 返回描述信息
     */
    String getMsg();
}
