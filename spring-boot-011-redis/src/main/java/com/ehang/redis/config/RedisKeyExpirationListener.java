package com.ehang.redis.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 监听所有db的过期事件__keyevent@*__:expired"
 *
 * @author 一行Java
 * @title: RedisKeyExpirationListener
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/8/5 16:36
 */
@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对 redis 数据失效事件，进行数据处理
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {

        // 获取到失效的 key，进行取消订单业务处理
        // 由于这里是监听了所有的key，如果只处理特定数据的话，需要做特殊处理
        String expiredKey = message.toString();
        log.info("过期的Key：{}", expiredKey);
    }
}
