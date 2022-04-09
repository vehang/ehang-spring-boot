package com.ehang.mysql.mybatis.plus;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
public class UpdateTest {
    @Autowired
    UserInfoService userInfoService;

//    // 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
//    boolean update(Wrapper<T> updateWrapper);
//    // 根据 whereWrapper 条件，更新记录
//    boolean update(T updateEntity, Wrapper<T> whereWrapper);
//    // 根据 ID 选择修改
//    boolean updateById(T entity);
//    // 根据ID 批量更新
//    boolean updateBatchById(Collection<T> entityList);
//    // 根据ID 批量更新
//    boolean updateBatchById(Collection<T> entityList, int batchSize);

    @Test
    public void update() {
        // 不建议使用，有
        // 以下的setSql和set选一个即可，务必要设置条件 否则有全部修改的风险
        //updateWrapper.setSql("user_name = '张三'");
        LambdaUpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<UserInfo>()
                .lambda()
                .set(UserInfo::getUserName, "一行Java（改1）")
                .eq(UserInfo::getId, 1);
        boolean update = userInfoService.update(updateWrapper);
        log.info("根据UpdateWrapper修改（不推荐使用）：{}", update);
    }

    @Test
    public void update2() {
        // 将符合UpdateWrapper全部修改为entity的值
        LambdaUpdateWrapper<UserInfo> updateWrapper1 = new UpdateWrapper<UserInfo>()
                .lambda()
                .eq(UserInfo::getUserName, "一行Java（改1）");
        UserInfo wangwu = new UserInfo(1, "一行Java（改2）", 10, (byte) 1);
        boolean update = userInfoService.update(wangwu, updateWrapper1);
        log.info("根据UpdateWrapper修改为指定对象：{}", update);
    }

    // 根据对象ID进行修改
    @Test
    public void updateById() {
        UserInfo wangwu = new UserInfo(1, "一行Java（改2）", 10, (byte) 1);
        boolean update = userInfoService.updateById(wangwu);
        log.info("根据对象ID修改：{}", update);
    }

    // 根据ID批量修改数据
    @Test
    public void updateBatchById() {
        UserInfo u1 = new UserInfo(1, "一行Java 1", 10, (byte) 1);
        UserInfo u2 = new UserInfo(2, "一行Java 2", 20, (byte) 1);
        UserInfo u3 = new UserInfo(3, "一行Java 3", 30, (byte) 1);
        List<UserInfo> us = new ArrayList<>();
        us.add(u1);
        us.add(u2);
        us.add(u3);
        boolean update = userInfoService.updateBatchById(us);
        log.info("根据对象ID批量修改：{}", update);
    }

    // 根据ID批量修改数据,每个批次的数量由后面的batchSize指定
    @Test
    public void updateBatchById2() {
        UserInfo u1 = new UserInfo(1, "一行Java 1", 10, (byte) 1);
        UserInfo u2 = new UserInfo(2, "一行Java 2", 20, (byte) 1);
        UserInfo u3 = new UserInfo(3, "一行Java 3", 30, (byte) 1);
        List<UserInfo> us = new ArrayList<>();
        us.add(u1);
        us.add(u2);
        us.add(u3);
        boolean update = userInfoService.updateBatchById(us, 2);
        log.info("根据对象ID批量修改：{}", update);
    }
}
