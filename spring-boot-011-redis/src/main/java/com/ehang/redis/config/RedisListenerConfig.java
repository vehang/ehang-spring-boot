package com.ehang.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author LENOVO
 * @title: RedisListenerConfig
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/8/5 16:35
 */

//@Configuration
//public class RedisListenerConfig {
//
//    @Bean
//    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
//
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        return container;
//    }
//}