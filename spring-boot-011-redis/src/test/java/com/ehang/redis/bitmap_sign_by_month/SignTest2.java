package com.ehang.redis.bitmap_sign_by_month;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

/**
 * @author 一行Java
 * @title: SignTest2
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/19 12:06
 */
@SpringBootTest
@Slf4j
public class SignTest2 {
    @Autowired
    private SignByMonthServiceImpl signByMonthService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 测试用户按月签到
     */
    @Test
    public void querySignDay() {
        //模拟用户签到
        //for(int i=5;i<19;i++){
        redisTemplate.opsForValue().setBit("UserId:Sign:560:2022-08", 2, true);
        //}

        log.info("560用户今日是否已签到:" + this.signByMonthService.checkSign("560"));
        Map<String, Boolean> stringBooleanMap = this.signByMonthService.querySignedInMonth("560");
        log.info("本月签到情况:");
        for (Map.Entry<String, Boolean> entry : stringBooleanMap.entrySet()) {
            log.info(entry.getKey() + ": " + (entry.getValue() ? "√" : "-"));
        }
        long countSignedInDayOfMonth = this.signByMonthService.countSignedInDayOfMonth("560");
        log.info("本月一共签到:" + countSignedInDayOfMonth + "天");
        log.info("目前连续签到:" + this.signByMonthService.queryContinuousSignCount("560", 7) + "天");
    }
}
