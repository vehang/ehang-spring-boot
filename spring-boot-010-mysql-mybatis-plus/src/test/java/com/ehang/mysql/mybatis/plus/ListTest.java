package com.ehang.mysql.mybatis.plus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author ehang
 * @title: ListTest
 * @projectName spring-boot-010-mysql-mybatis-plus
 * @description: TODO
 * @date 2021/11/28 13:18
 */
@SpringBootTest
@Slf4j
public class ListTest {
    @Autowired
    UserInfoService userInfoService;

//    // 查询所有
//    List<T> list();
//    // 查询列表
//    List<T> list(Wrapper<T> queryWrapper);
//    // 查询（根据ID 批量查询）
//    Collection<T> listByIds(Collection<? extends Serializable> idList);
//    // 查询（根据 columnMap 条件）
//    Collection<T> listByMap(Map<String, Object> columnMap);
//    // 查询所有列表
//    List<Map<String, Object>> listMaps();
//    // 查询列表
//    List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper);
//    // 查询全部记录
//    List<Object> listObjs();
//    // 查询全部记录
//    <V> List<V> listObjs(Function<? super Object, V> mapper);
//    // 根据 Wrapper 条件，查询全部记录
//    List<Object> listObjs(Wrapper<T> queryWrapper);
//    // 根据 Wrapper 条件，查询全部记录
//    <V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);

    @Test
    void list() {
        List<UserInfo> list = userInfoService.list();
        log.info("查询所有：{}", list);
    }

    @Test
    void listByWrapper() {
        // 根据条件查询所有数据
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                .lambda()
                .ge(UserInfo::getId, 100);
        List<UserInfo> list = userInfoService.list(lambdaQueryWrapper);
        log.info("按条件查询所有：{}", list);
    }

    @Test
    void listByIds() {
        List<UserInfo> userInfos = userInfoService.listByIds(Arrays.asList(1, 2, 3));
        log.info("按根据id批量查询：{}", userInfos);
    }

    @Test
    void listByMap() {
        Map<String, Object> maps = new HashMap<>();
        maps.put("user_name", "一行Java 1");
        List<UserInfo> userInfos = userInfoService.listByMap(maps);
        log.info("指定字段批量查询：{}", userInfos);

        // 以上的查询方式等价于下面这种写法，这种写法更优雅
        List<UserInfo> list = userInfoService.list(
                new QueryWrapper<UserInfo>()
                        .lambda()
                        .eq(UserInfo::getUserName, "一行Java 1")
        );
        log.info("指定字段批量查询：{}", list);
    }

    @Test
    void listMaps() {
        List<Map<String, Object>> maps = userInfoService.listMaps();
        log.info("查询所有,以Map的方式返回：{}", maps);
    }

    @Test
    void listMapsByWrapper() {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                .lambda()
                .ge(UserInfo::getId, 100);
        List<Map<String, Object>> maps = userInfoService.listMaps(lambdaQueryWrapper);
        log.info("查询所有,以Map的方式返回：{}", maps);
    }

    @Test
    void listObjs() {
        List<Object> objects = userInfoService.listObjs();
        log.info("查询所有,返回数据的第一列：{}", objects);
    }

    @Test
    void listObjsByWrapper() {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new QueryWrapper<UserInfo>()
                .lambda()
                .ge(UserInfo::getId, 100);
        List<Object> objects = userInfoService.listObjs(lambdaQueryWrapper);
        log.info("按条件查询所有,返回数据的第一列：{}", objects);
    }

    @Test
    void listMapper() {
        Function f = new Function<Object, String>() {
            @Override
            public String apply(Object obj) {
                if (obj instanceof UserInfo) {
                    UserInfo user = (UserInfo) obj;
                    return user.getUserName();
                }
                return obj.toString();
            }
        };

        List<String> strings = userInfoService.listObjs(f);
        log.info("按条件查询所有,返回数据的第一列：{}", strings);

    }
}
