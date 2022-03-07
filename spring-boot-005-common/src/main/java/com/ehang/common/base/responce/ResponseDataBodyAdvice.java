package com.ehang.common.base.responce;

import com.ehang.common.base.dto.BaseResponceDto;
import com.ehang.common.utils.ReturnUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;

/**
 * @author ehang
 * @title: ResponseDataBodyAdvice
 * @projectName ehang-spring-boot
 * @description: TODO 响应数据的切面 目的是为了规范响应数据的格式
 * @date 2022/3/6 16:49
 */
@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class ResponseDataBodyAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 得到自定义的注解
     */
    private static final Class<? extends Annotation> ANNOTATION_TYPE = ResponseDataBody.class;

    /**
     * 判断类或者方法是否使用了 @ResponseDataBody
     * 这里将注解添加在BaseController上面；以为着只要继承了BaseController的Controller都使用了该注解
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ANNOTATION_TYPE) || returnType.hasMethodAnnotation(ANNOTATION_TYPE);
    }

    /**
     * 当类或者方法使用了 @ResponseDataBody 也就是上面的方法返回的true 就会调用这个方法
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 防止重复包裹的问题出现 如果已经是要返回的基础对象了 就直接返回
        if (null != body && body instanceof BaseResponceDto) {
            return body;
        }
        // 如果不是基础的响应对象，需要进行结构封装
        return ReturnUtils.success(body);
    }
}
