package com.ehang.redis.controller;

import com.ehang.redis.lock.byannotation.anno.RedisLockAnnotation;
import com.ehang.redis.lock.byannotation.enums.RedisLockTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LENOVO
 * @title: RedisLockController
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/9/7 10:23
 */
@RestController
@Slf4j
public class RedisLockController {

    @GetMapping("/testRedisLock1")
    @RedisLockAnnotation(typeEnum = RedisLockTypeEnum.ORDER_MANA, lockTime = 3, tryLockCount = 3)
    public String testRedisLock1(@RequestParam("orderNo") Long orderNo) {
        try {
            log.info("orderNo:{} 睡眠执行前:{}", orderNo, System.currentTimeMillis());
            Thread.sleep(5000);
            log.info("orderNo:{} 睡眠执行后:{}", orderNo, System.currentTimeMillis());
        } catch (Exception e) {
            // log error
            log.info("has some error", e);
        }
        return orderNo + " ok!";
    }

    @GetMapping("/testRedisLock2")
    @RedisLockAnnotation(typeEnum = RedisLockTypeEnum.ORDER_MANA, lockTime = 3, tryLockCount = 3)
    public String testRedisLock2(@RequestParam("orderNo") Long orderNo) {
        try {
            log.info("orderNo:{} 睡眠执行前:{}", orderNo, System.currentTimeMillis());
            Thread.sleep(5000);
            log.info("orderNo:{} 睡眠执行后:{}", orderNo, System.currentTimeMillis());
        } catch (Exception e) {
            // log error
            log.info("has some error", e);
        }
        return orderNo + " ok!";
    }
}
