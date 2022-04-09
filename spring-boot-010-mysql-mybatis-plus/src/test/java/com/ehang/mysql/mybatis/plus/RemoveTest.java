package com.ehang.mysql.mybatis.plus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

/**
 *
 */
@SpringBootTest
@Slf4j
public class RemoveTest {
    @Autowired
    UserInfoService userInfoService;

//    // 根据 entity 条件，删除记录
//    boolean remove(Wrapper<T> queryWrapper);
//    // 根据 ID 删除
//    boolean removeById(Serializable id);
//    // 根据 columnMap 条件，删除记录
//    boolean removeByMap(Map<String, Object> columnMap);
//    // 删除（根据ID 批量删除）
//    boolean removeByIds(Collection<? extends Serializable> idList);

    @Test
    void remove() {
        // 根据条件删除
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserName, "张三");
        boolean remove = userInfoService.remove(queryWrapper);
        log.info("根据条件删除用户数据：{}", remove);
    }

    @Test
    void removeById() {
        // 根据主键id删除
        boolean removeById = userInfoService.removeById(1006);
        log.info("根据主键ID删除用户数据：{}", removeById);

    }

    @Test
    void removeByMap() {
        // 根据列的值删除
        Map<String, Object> cms = new HashMap();
        cms.put("user_name", "李四");
        cms.put("source", 1);
        boolean removeByMap = userInfoService.removeByMap(cms);

        log.info("根据字段值删除用户数据：{}", removeByMap);
    }

    @Test
    void removeByIds() {
        // 根据主键id批量删除
        List<Integer> ids = Arrays.asList(new Integer[]{1004, 1005, 1006});
        boolean removeByIds = userInfoService.removeByIds(ids);
        log.info("根据主键ids批量删除用户数据：", removeByIds);
    }
}
