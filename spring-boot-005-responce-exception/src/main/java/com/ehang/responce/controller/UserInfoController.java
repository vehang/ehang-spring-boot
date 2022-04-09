package com.ehang.responce.controller;

import com.alibaba.fastjson.JSON;
import com.ehang.common.base.controller.BaseController;
import com.ehang.common.base.dto.BaseResponceDto;
import com.ehang.common.base.exception.BaseException;
import com.ehang.common.base.responce.ResponseDataBody;
import com.ehang.common.utils.ReturnUtils;
import com.ehang.responce.controller.dto.UserRequestDto;
import com.ehang.responce.service.UserService;
import com.ehang.responce.service.dto.UserInfoDto;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ehang
 * @title: ResponceController
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/6 17:06
 */
@RestController
@RequestMapping("/userInfo")
@Slf4j
public class UserInfoController extends BaseController {

    @Autowired
    UserService userService;

    /**
     * 测试参数异常后的格式响应
     *
     * @param requestDto
     * @return
     * @throws BaseException
     */
    @PostMapping
    public BaseResponceDto add(
            @Validated(UserRequestDto.AddValidate.class)
            @RequestBody UserRequestDto requestDto) throws BaseException {
        log.info("add requestDto:{}", JSON.toJSONString(requestDto));
        // 这里直接返回了BaseResponceDto
        return ReturnUtils.success();
    }

    /**
     * 获取用户列表
     *
     * @return
     * @throws BaseException
     */
    @JsonView(UserInfoDto.UserInfoListView.class)
    @GetMapping
    public List<UserInfoDto> list() throws BaseException {
        // 这里返回的是数据List
        // ResponseDataBodyAdvice会拦截响应，会对数据进行包装，来统一响应格式
        return userService.list();
    }

    /**
     * 获取用户详情
     *
     * @param id
     * @return
     * @throws BaseException
     */
    @JsonView(UserInfoDto.UserInfoDetailView.class)
    @GetMapping("{id}")
    @ResponseDataBody
    public UserInfoDto detail(@PathVariable Integer id) throws BaseException {
        return userService.detail(id);
    }

    /**
     * 测试返回void时  ResponseDataBodyAdvice是否拦截封装
     *
     * @param id
     * @throws BaseException
     */
    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) throws BaseException {
        // 去做业务
    }
}
