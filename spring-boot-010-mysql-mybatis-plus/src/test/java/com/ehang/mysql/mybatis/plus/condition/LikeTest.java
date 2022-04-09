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
public class LikeTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void like() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .like(UserInfo::getUserName, "一行Java")
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (user_name LIKE "%一行Java%")
    }

    @Test
    void notLike() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .notLike(UserInfo::getUserName, "一行Java")
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (user_name NOT LIKE "%一行Java%")
    }

    @Test
    void likeLeft() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .likeLeft(UserInfo::getUserName, "一行Java")
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (user_name LIKE "%一行Java")
    }

    @Test
    void likeRight() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .likeRight(UserInfo::getUserName, "一行Java")
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (user_name LIKE "一行Java%")
    }
}
