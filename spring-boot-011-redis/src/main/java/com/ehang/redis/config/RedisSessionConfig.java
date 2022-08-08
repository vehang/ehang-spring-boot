package com.ehang.redis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author 开启session共享
 * @title: RedisSessionConfig
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/8/5 15:57
 */
@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {
}
