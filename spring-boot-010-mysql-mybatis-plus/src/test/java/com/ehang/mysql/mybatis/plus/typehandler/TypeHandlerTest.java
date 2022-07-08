package com.ehang.mysql.mybatis.plus.typehandler;

import com.ehang.mysql.mybatis.plus.generator.cls.demain.ClassInfo;
import com.ehang.mysql.mybatis.plus.generator.cls.service.ClassInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ehang
 * @title: TypeHandlerTest
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/4/11 11:10
 */
@SpringBootTest
@Slf4j
public class TypeHandlerTest {
    @Autowired
    ClassInfoService classInfoService;

    @Test
    public void typeHandler(){
        ClassInfo byId = classInfoService.getById(1);
        log.info("{}",byId);
    }

}
