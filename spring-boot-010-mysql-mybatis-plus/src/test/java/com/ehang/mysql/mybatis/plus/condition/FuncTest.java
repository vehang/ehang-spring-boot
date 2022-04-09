package com.ehang.mysql.mybatis.plus.condition;

import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 *
 */
@SpringBootTest
@Slf4j
public class FuncTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void func() {
        Boolean condition = false;
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .func(i -> {
                    if (condition) {
                        i.eq(UserInfo::getId, 10);
                    } else {
                        i.eq(UserInfo::getId, 100);
                    }
                }).list();
        log.info("userInfos:{}", userInfos);
        //func(i -> {if (true) {i.eq(UserInfo::getId, 10);} else {i.eq(UserInfo::getId, 100); }})
    }
}
