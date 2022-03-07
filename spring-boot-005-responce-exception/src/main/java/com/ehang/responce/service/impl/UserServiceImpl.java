package com.ehang.responce.service.impl;

import com.ehang.common.base.exception.BaseException;
import com.ehang.responce.service.UserService;
import com.ehang.responce.service.dto.UserInfoDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LENOVO
 * @title: UserServiceImpl
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/6 22:40
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<UserInfoDto> list() throws BaseException {
        // 这里为了简单，直接模拟产生5条数据
        // 实际业务中，这里大部分都会去数据库获取数据并展示
        List<UserInfoDto> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            UserInfoDto userInfoDto = new UserInfoDto();
            userInfoDto.setId(i);
            userInfoDto.setUserName("name" + i);
            userInfoDto.setAge(i);
            userInfoDto.setPassWord(String.valueOf(System.currentTimeMillis()));
            list.add(userInfoDto);
        }
        return list;
    }

    @Override
    public UserInfoDto detail(Integer id) throws BaseException {
        // 这里为了简单，直接实例化一个对象返回
        // 实际业务 需要从数据库中获取
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setId(id);
        userInfoDto.setUserName("name" + id);
        userInfoDto.setAge(id);
        userInfoDto.setPassWord("123456789");
        return userInfoDto;
    }
}
