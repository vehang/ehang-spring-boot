package com.ehang.redis.set_like;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 一行Java
 * @title: LikeMain
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/18 15:38
 */
@Slf4j
@SpringBootTest
public class LikeMain {
    private final String KEY_LIKE_ARTICLE_PROFIX = "like:article:";

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void test() {
        long articleId = 100;
        Long likeNum = like(articleId, 1001, 1002, 2001, 3005, 4003);
        unLike(articleId, 2001);
        likeNum = likeNum(articleId);
        boolean b2001 = isLike(articleId, 2001);
        boolean b3005 = isLike(articleId, 3005);
        log.info("文章：{} 点赞数量：{} 用户2001的点赞状态：{} 用户3005的点赞状态：{}", articleId, likeNum, b2001, b3005);
    }

    /**
     * 点赞
     *
     * @param articleId 文章ID
     * @return 点赞数量
     */
    public Long like(Long articleId, Integer... userIds) {
        String key = KEY_LIKE_ARTICLE_PROFIX + articleId;
        Long add = redisTemplate.opsForSet().add(key, userIds);
        return add;
    }

    public Long unLike(Long articleId, Integer... userIds) {
        String key = KEY_LIKE_ARTICLE_PROFIX + articleId;
        Long remove = redisTemplate.opsForSet().remove(key, userIds);
        return remove;
    }

    public Long likeNum(Long articleId) {
        String key = KEY_LIKE_ARTICLE_PROFIX + articleId;
        Long size = redisTemplate.opsForSet().size(key);
        return size;
    }

    public Boolean isLike(Long articleId, Integer userId) {
        String key = KEY_LIKE_ARTICLE_PROFIX + articleId;
        return redisTemplate.opsForSet().isMember(key, userId);
    }

}
