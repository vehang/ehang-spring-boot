package com.ehang.redis.lock.byannotation.anno;

import com.ehang.redis.lock.byannotation.enums.RedisLockTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LENOVO
 * @title: RedisLockAnnotation
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/9/7 10:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RedisLockAnnotation {
    /**
     * 特定参数识别，默认取第 0 个下标
     */
    int lockFiled() default 0;

    /***
     * 尝试拿锁的次数
     * @return
     */
    int tryLockCount() default 0;

    /**
     * 超时重试次数
     */
    int tryCount() default 3;

    /**
     * 自定义加锁类型
     */
    RedisLockTypeEnum typeEnum();

    /**
     * 释放时间，秒 s 单位
     */
    long lockTime() default 30;
}
