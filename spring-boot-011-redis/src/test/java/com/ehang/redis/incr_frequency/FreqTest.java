package com.ehang.redis.incr_frequency;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author 一行Java
 * @title: 基于Redis的简单限流
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/8/2 9:43
 */
@SpringBootTest
@Slf4j
public class FreqTest {
    // 单位时间（秒）
    private static final Integer TIME = 5;
    // 允许访问上限次数
    private static final Integer MAX = 100;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        String userName = "user1";

        int tag = 1;

        boolean frequency = frequency(userName);
        log.info("{} 第{}次请求是否放行：{}", userName, tag, frequency);
        for (int i = 0; i < 100; i++) {
            tag += 1;
            frequency(userName);
        }
        frequency = frequency(userName);
        log.info("{} 第{}次请求是否放行：{}", userName, tag, frequency);

        Thread.sleep(5000);
        frequency = frequency(userName);
        log.info("模拟等待5s后，{} 第{}次请求是否放行：{}", userName, tag, frequency);
    }

    /**
     * 校验访问频率
     *
     * @param uniqueId 用于限流的唯一ID 可以是用户ID、或者客户端IP等
     * @return true：放行  false：拦截
     */
    private boolean frequency(String uniqueId) {
        String key = "r:q:" + uniqueId;
        Long increment = redisTemplate.opsForValue().increment(key);
        if (increment == 1) {
            redisTemplate.expire(key, TIME, TimeUnit.SECONDS);
        }

        if (increment <= MAX) {
            return true;
        }

        return false;
    }
}
