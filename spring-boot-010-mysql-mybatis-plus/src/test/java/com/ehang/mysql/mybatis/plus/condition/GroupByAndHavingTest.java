package com.ehang.mysql.mybatis.plus.condition;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * 条件 分组
 */
@SpringBootTest
@Slf4j
public class GroupByAndHavingTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void groupByAndHaving() {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.select("source,count(id) as sum")
                .groupBy("source")
                .having("count(id) > {0}", 35);
        List<Map<String, Object>> maps = userInfoService.listMaps(userInfoQueryWrapper);

        // 等价sql：SELECT source,count(id) as sum FROM user_info GROUP BY source HAVING count(id) > 35
    }
}
