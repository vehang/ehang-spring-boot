package com.ehang.mysql.mybatis.plus.generator.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.mapper.UserInfoMapper;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {
}




