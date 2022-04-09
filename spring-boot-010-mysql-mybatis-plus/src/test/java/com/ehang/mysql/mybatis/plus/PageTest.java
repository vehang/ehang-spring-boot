package com.ehang.mysql.mybatis.plus;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @author ehang
 * @title: PageTest
 * @projectName spring-boot-010-mysql-mybatis-plus
 * @description: TODO
 * @date 2021/11/24 9:12
 */
@SpringBootTest
@Slf4j
public class PageTest {

    @Autowired
    UserInfoService userInfoService;

//    // 无条件分页查询
//    IPage<T> page(IPage<T> page);
//    // 条件分页查询
//    IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper);
//    // 无条件分页查询
//    IPage<Map<String, Object>> pageMaps(IPage<T> page);
//    // 条件分页查询
//    IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper);

    @Test
    void page() {
        // 分页查询；结果以对象方式返回
        Page<UserInfo> page = userInfoService.page(new Page<UserInfo>(2, 5));
        log.info("page:{}", JSON.toJSONString(page));
    }

    @Test
    void pageByWrapper() {
        // 带查询条件的分页查询; 结果以对象方式返回
        // 查询条件是id大于10
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                .lambda()
                .ge(UserInfo::getId, 10);
        Page<UserInfo> page = userInfoService.page(new Page<UserInfo>(2, 5), lambdaQueryWrapper);
        log.info("pageByWrapper:{}", JSON.toJSONString(page));
    }

    @Test
    void pageMaps() {
        // 分页查询；以Map的方式返回
        Page<Map<String, Object>> page = userInfoService.pageMaps(new Page(2, 5));
        log.info("pageMaps:{}", JSON.toJSONString(page));
    }

    @Test
    void pageMapsByWrapper() {
        // 带查询条件的分页查询，结果以Map方式返回
        // 查询条件是id大于10
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                .lambda()
                .ge(UserInfo::getId, 10);
        Page<Map<String, Object>> page = userInfoService.pageMaps(new Page(2, 5), lambdaQueryWrapper);
        log.info("pageMapsByWrapper:{}", JSON.toJSONString(page));
    }
}
