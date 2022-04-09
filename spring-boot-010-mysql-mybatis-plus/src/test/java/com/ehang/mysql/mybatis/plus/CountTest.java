package com.ehang.mysql.mybatis.plus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ehang
 * @title: CountTest
 * @projectName spring-boot-010-mysql-mybatis-plus
 * @description: TODO
 * @date 2021/11/28 16:05
 */
@SpringBootTest
@Slf4j
public class CountTest {
    @Autowired
    UserInfoService userInfoService;

//    // 查询总记录数
//    Long count();
//    // 根据 Wrapper 条件，查询总记录数
//    Long count(Wrapper<T> queryWrapper);

    @Test
    void count() {
        Long count = userInfoService.count();
        log.info("总数：{}", count);
    }

    @Test
    void countByWrapper() {
        Long count = userInfoService.count(new QueryWrapper<UserInfo>()
                .lambda()
                .ge(UserInfo::getId, 100));
        log.info("按条件查询总数：{}", count);
    }
}
