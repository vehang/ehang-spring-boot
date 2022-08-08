package com.ehang.redis.incr_pv;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author LENOVO
 * @title: PVMain
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/18 15:02
 */
@SpringBootTest
@Slf4j
public class PVMain {
    private final String KEY_UV_PAGE_PROFIX = "pv:page:";
    private final String KEY_PAGE_PROFIX = "page:";

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void test() {
        int page1000 = 1000;
        long pv = pvByString(page1000);
        log.info("页面：{} 的pv：{}", page1000, pv);

        int page1001 = 1001;
        pv = pvByHash(page1000);
        log.info("页面：{} 的pv：{}", page1001, pv);
    }

    /**
     * 通过string
     *
     * @param pageId
     * @return
     */
    public Long pvByString(Integer pageId) {
        String key = KEY_UV_PAGE_PROFIX + pageId;
        Long increment = redisTemplate.opsForValue().increment(key);
        return increment;
    }

    /**
     * 通过hash
     *
     * @param pageId
     * @return
     */
    public Long pvByHash(Integer pageId) {
        String key = KEY_PAGE_PROFIX + pageId;
        Long increment = redisTemplate.opsForHash().increment(key, "pv", 1);
        return increment;
    }
}
