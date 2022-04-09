package com.ehang.mysql.mybatis.plus;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
public class SaveOrUpdateTest {
    @Autowired
    UserInfoService userInfoService;

//    // TableId 注解存在更新记录，否插入一条记录
//    boolean saveOrUpdate(T entity);
//    // 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
//    boolean saveOrUpdate(T entity, Wrapper<T> updateWrapper);
//    // 批量修改插入
//    boolean saveOrUpdateBatch(Collection<T> entityList);
//    // 批量修改插入
//    boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize);

    @Test
    void saveOrUpdate() {
        // 单个修改
        UserInfo userInfo = new UserInfo(1004, "张三(改)", 20, (byte) 1);
        boolean saveOrUpdate = userInfoService.saveOrUpdate(userInfo);
        log.info("单条插入(或修改)的结果：{}", saveOrUpdate);

        // 根据条件修改
        LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInfo::getSource, 1);
        boolean saveOrUpdateByWrapper = userInfoService.saveOrUpdate(userInfo, updateWrapper);
        log.info("单条插入(或根据条件修改)的结果：{}", saveOrUpdateByWrapper);

        // 批量插入
        UserInfo lisi = new UserInfo(1005, "李四（改）", 10, (byte) 1);
        UserInfo wangwu = new UserInfo(1006, "王五（改）", 10, (byte) 1);
        List<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(lisi);
        userInfos.add(wangwu);
        boolean saveBatch = userInfoService.saveOrUpdateBatch(userInfos, 10);
        log.info("批量插入(或修改)的结果：{}", saveBatch);
    }
}
