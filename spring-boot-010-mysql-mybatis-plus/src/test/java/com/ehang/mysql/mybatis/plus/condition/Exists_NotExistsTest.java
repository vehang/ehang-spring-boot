package com.ehang.mysql.mybatis.plus.condition;

import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Exists NotExists
 */
@SpringBootTest
@Slf4j
public class Exists_NotExistsTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void exists(){
        userInfoService.query()
                .exists("select id from user_info where id > 1000").list();
        //log.info("userInfos:{}",userInfos);

    }
}
