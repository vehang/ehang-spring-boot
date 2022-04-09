package com.ehang.mysql.mybatis.plus.condition;

import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 条件 等于 eq
 */
@SpringBootTest
@Slf4j
public class EqTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void eq() {
        UserInfo userInfo = userInfoService.lambdaQuery()
                .eq(UserInfo::getId, 1)
                .one();
        log.info("userInfo:{}", userInfo);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id = 1)
    }

    @Test
    void eqConditionFalse() {
        // condition等于false 忽略所有的条件 也就是说eq设置不会生效
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .eq(false, UserInfo::getId, 1)
                .list();
        log.info("userInfo:{}", userInfos);

        // 等价sql：SELECT id,user_name,age,source FROM user_info
    }
}
