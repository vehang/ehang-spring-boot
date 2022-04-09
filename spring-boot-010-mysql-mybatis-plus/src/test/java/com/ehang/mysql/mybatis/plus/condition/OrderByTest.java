package com.ehang.mysql.mybatis.plus.condition;

import com.alibaba.fastjson.JSON;
import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 条件 不等于 ne
 */
@SpringBootTest
@Slf4j
public class OrderByTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void orderBy() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .orderBy(true, true, UserInfo::getSource)
                .list();
        log.info("userInfos:{}", JSON.toJSONString(userInfos));
        // 等价sql：SELECT id,user_name,age,source FROM user_info ORDER BY source ASC
    }

    @Test
    void orderByAsc() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .orderByAsc(UserInfo::getSource)
                .list();
        log.info("userInfos:{}", JSON.toJSONString(userInfos));
        // 等价sql：SELECT id,user_name,age,source FROM user_info ORDER BY source ASC
    }

    @Test
    void orderByDesc() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .orderByDesc(UserInfo::getSource)
                .list();
        log.info("userInfos:{}", JSON.toJSONString(userInfos));
        // 等价sql：SELECT id,user_name,age,source FROM user_info ORDER BY source DESC
    }
}
