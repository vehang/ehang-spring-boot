package com.ehang.common.base.responce;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * @author ehang
 * @title: ResponseDataBody
 * @projectName ehang-spring-boot
 * @description: TODO 规范响应数据的注解
 * @date 2022/3/6 16:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ResponseBody
public @interface ResponseDataBody {
}
