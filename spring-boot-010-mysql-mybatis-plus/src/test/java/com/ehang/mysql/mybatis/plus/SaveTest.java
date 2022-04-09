package com.ehang.mysql.mybatis.plus;

import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@SpringBootTest
@Slf4j
public class SaveTest {
    @Autowired
    UserInfoService userInfoService;

//    // 插入一条记录（选择字段，策略插入）
//    boolean save(T entity);
//    // 插入（批量）
//    boolean saveBatch(Collection<T> entityList);
//    // 插入（批量）
//    boolean saveBatch(Collection<T> entityList, int batchSize);

    // 单个插入
    @Test
    void save() {
        UserInfo userInfo = new UserInfo(null, "张三", 10, (byte) 1);
        boolean save = userInfoService.save(userInfo);
        log.info("单条添加的结果：{}", save);
    }

    // 批量插入
    @Test
    void saveBatch() {
        UserInfo lisi = new UserInfo(null, "李四", 10, (byte) 1);
        UserInfo wangwu = new UserInfo(null, "王五", 10, (byte) 1);
        List<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(lisi);
        userInfos.add(wangwu);
        boolean saveBatch = userInfoService.saveBatch(userInfos, 10);
        log.info("批量添加的结果：{}", saveBatch);
    }
}
