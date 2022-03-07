package com.ehang.responce.service;

import com.ehang.common.base.exception.BaseException;
import com.ehang.responce.service.dto.UserInfoDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ehang
 * @title: UserService
 * @projectName ehang-spring-boot
 * @description: TODO 用户业务测试service
 * @date 2022/3/6 22:16
 */
@Service
public interface UserService {

    /**
     * 用户列表
     *
     * @return
     * @throws BaseException
     */
    List<UserInfoDto> list() throws BaseException;

    /**
     * 用户详情
     *
     * @return
     * @throws BaseException
     */
    UserInfoDto detail(Integer id) throws BaseException;
}
