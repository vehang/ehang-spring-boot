package com.ehang.common.base.dto;

import com.ehang.common.base.status.IStatusCode;
import com.ehang.common.base.exception.BaseException;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author LENOVO
 * @title: BaseResponceDto
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/6 16:36
 */
@Data
public class BaseResponceDto<T> {
    /**
     * 状态码
     */
    @JsonView(ResponceBaseDtoView.class)
    private Integer status;
    /**
     * 状态描述
     */
    @JsonView(ResponceBaseDtoView.class)
    private String msg;
    /**
     * 响应数据
     */
    @JsonView(ResponceBaseDtoView.class)
    private T data;

    /**
     * 只有状态码的响应
     *
     * @param statusCode
     */
    public BaseResponceDto(IStatusCode statusCode) {
        if (null != statusCode) {
            this.status = statusCode.getStatus();
            this.msg = statusCode.getMsg();
        }
    }

    /**
     * 有状态码且有参数的响应
     *
     * @param statusCode
     * @param data
     */
    public BaseResponceDto(IStatusCode statusCode, T data) {
        if (null != statusCode) {
            this.status = statusCode.getStatus();
            this.msg = statusCode.getMsg();
        }
        if (null != data) {
            this.data = data;
        }
    }

    /**
     * 根据HttpStatus响应
     *
     * @param httpStatus http请求状态码
     */
    public BaseResponceDto(HttpStatus httpStatus) {
        if (null != httpStatus) {
            this.status = httpStatus.value();
            this.msg = httpStatus.getReasonPhrase();
        }
    }

    /**
     * 根据http状态码返回 并返回额外返回数据
     *
     * @param httpStatus http状态码
     * @param data       数据
     */
    public BaseResponceDto(HttpStatus httpStatus, T data) {
        if (null != httpStatus) {
            this.status = httpStatus.value();
            this.msg = httpStatus.getReasonPhrase();
        }
        if (null != data) {
            this.data = data;
        }
    }

    /**
     * 根据异常响应错误码
     *
     * @param baseException 异常对象
     */
    public BaseResponceDto(BaseException baseException) {
        if (null != baseException) {
            this.status = baseException.getError();
            this.msg = baseException.getMsg();
            this.data = (T) baseException.getData();
        }
    }

    /**
     * 响应数据最外层的视图 也是所有响应视图的父类
     */
    public interface ResponceBaseDtoView {
    }
}