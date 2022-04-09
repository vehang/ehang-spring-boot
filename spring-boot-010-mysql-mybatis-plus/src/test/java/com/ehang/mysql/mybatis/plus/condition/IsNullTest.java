package com.ehang.mysql.mybatis.plus.condition;

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
public class IsNullTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void isNull() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .isNull(UserInfo::getUserName)
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (user_name IS NULL)
    }

    @Test
    void isNotNull() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .isNotNull(UserInfo::getUserName)
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (user_name IS NOT NULL)
    }
}
