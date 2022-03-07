package com.ehang.common.base.controller;

import com.ehang.common.base.dto.ParamErrDto;
import com.ehang.common.base.exception.BaseException;
import com.ehang.common.base.responce.ResponseDataBody;
import com.ehang.common.base.status.BaseStatusCode;
import com.ehang.common.utils.ReturnUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ehang
 * @title: BaseController
 * @projectName ehang-spring-boot
 * @description: TODO Controller的的基础对象 所有的Controller都将继承自他
 * @date 2022/3/6 16:46
 */
@Slf4j
@ResponseDataBody
public class BaseController {

    /**
     * 未获取到参数的异常
     *
     * @param httpMessageNotReadableException
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException httpMessageNotReadableException) {
        log.error("捕获请求参数读取异常：", httpMessageNotReadableException);
        // 前端未传递参数 导致读取参数异常
        return ReturnUtils.error(BaseStatusCode.ERR_1000);
    }

    /**
     * validate 校验参数出现异常
     *
     * @param bindException
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object bindExceptionHandler(BindException bindException) {
        log.error("捕获请求参数校验异常：", bindException);
        // 获取到所有的校验失败的属性
        List<FieldError> fieldErrors = bindException.getFieldErrors();

        // 实例化一个用于装参数错误的list
        // 方便提示客户端 那些参数的格式不对
        List<ParamErrDto> paramErrDtos = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            // 那段字段名
            String field = fieldError.getField();
            // 拿到异常的描述
            String defaultMessage = fieldError.getDefaultMessage();
            log.warn("field:{} msg:{}", field, defaultMessage);
            // 添加到list中去
            paramErrDtos.add(new ParamErrDto(field, defaultMessage));
        }

        // 返回前端参数错误 并告诉前端那些字段不对 具体描述是什么
        return ReturnUtils.error(BaseStatusCode.ERR_1000, paramErrDtos);
    }

    /**
     * 业务异常
     *
     * @param baseException
     * @return
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object baseExceptionHandler(BaseException baseException) {
        log.error("捕获到业务异常：", baseException);
        // 基础的业务异常
        return ReturnUtils.error(baseException);
    }

    /**
     * 通过ExceptionHandler 捕获controller未捕获到的异常，给用户一个友好的返回
     *
     * @param ex 异常信息
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object exceptionHandler(Exception ex) {
        log.error("exceptionHandler....");
        // 所有的  自定义的、已知的异常全部都没有匹配上
        // 直接响应响应一个未知错误的提醒
        return ReturnUtils.error(BaseStatusCode.ERR_9999);
    }
}
