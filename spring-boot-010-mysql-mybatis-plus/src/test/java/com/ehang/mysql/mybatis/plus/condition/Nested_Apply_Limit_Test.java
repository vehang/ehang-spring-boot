package com.ehang.mysql.mybatis.plus.condition;

import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Nested Apply Limit 测试
 */
@SpringBootTest
@Slf4j
public class Nested_Apply_Limit_Test {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void nested() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .ge(UserInfo::getId, 10)
                .nested(
                        i -> i.eq(UserInfo::getUserName, "张三").or(m -> m.ge(UserInfo::getId, 1005))
                )
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id <= 10 OR (user_name = "张三" AND id >= 1005))
    }

    @Test
    void apply() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .apply("id < {0}", 20)
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id < 20)
    }

    @Test
    void last() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .last("limit 1")
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info limit 1
    }
}
