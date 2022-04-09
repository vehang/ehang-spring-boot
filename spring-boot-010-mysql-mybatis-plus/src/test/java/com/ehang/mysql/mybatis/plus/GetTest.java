package com.ehang.mysql.mybatis.plus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @author ehang
 * @title: GetTest
 * @projectName spring-boot-010-mysql-mybatis-plus
 * @description: TODO
 * @date 2021/11/23 23:00
 */
@SpringBootTest
@Slf4j
public class GetTest {
    @Autowired
    UserInfoService userInfoService;

//    // 根据 ID 查询
//    T getById(Serializable id);
//    // 根据 Wrapper，查询一条记录。结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
//    T getOne(Wrapper<T> queryWrapper);
//    // 根据 Wrapper，查询一条记录
//    T getOne(Wrapper<T> queryWrapper, boolean throwEx);
//    // 根据 Wrapper，查询一条记录
//    Map<String, Object> getMap(Wrapper<T> queryWrapper);
//    // 根据 Wrapper，查询一条记录
//    <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);

    @Test
    void getById() {
        UserInfo userInfo = userInfoService.getById(1);
        log.info("根据ID查询用户信息:{}", userInfo);
    }

    // 查询一条数据，如果根据条件查询出了多条，则会报错
    @Test
    void getOne() {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                .lambda()
                .eq(UserInfo::getId, 1);
        UserInfo userInfo = userInfoService.getOne(lambdaQueryWrapper);
        log.info("根据ID查询单用户信息:{}", userInfo);
    }

    // 查询单条数据，如果返回多条数据则去取第一条返回
    @Test
    void getOne2() {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                .lambda()
                .eq(UserInfo::getUserName, "一行Java 1")
                .orderByDesc(UserInfo::getId);
        UserInfo userInfo = userInfoService.getOne(lambdaQueryWrapper, false);
        log.info("根据ID查询单用户信息:{}", userInfo);
    }

    // 查询单条数据 以Map的方式返回
    @Test
    void getMap() {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                .lambda()
                .eq(UserInfo::getId, 1);
        // String为数据库列名  Object为值
        Map<String, Object> map = userInfoService.getMap(lambdaQueryWrapper);
        log.info("根据ID查询单用户信息:{}", map);
    }

    // 查询返回结果的第一列
    @Test
    void getObj() {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                .lambda()
                .eq(UserInfo::getUserName, "一行Java 1")
                .select(UserInfo::getUserName);

        String obj = userInfoService.getObj(lambdaQueryWrapper, (u) -> u.toString());
        log.info("getObj:{}", obj);
    }
}
