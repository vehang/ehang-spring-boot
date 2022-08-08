package com.ehang.redis.set_tag_search;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author LENOVO
 * @title: TagSearch
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/8/2 14:59
 */
@SpringBootTest
@Slf4j
public class TagSearch {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test() {
        add("brand:apple", "iphone100");
        add("brand:huawei", "meta5000");
        add("ram:5t", "iphone100", "meta5000");
        add("ram:10t", "meta5000");
        add("os:ios", "iphone100");
        add("os:android", "meta5000");
        add("screensize:6.0-6.29", "iphone100", "meta5000");

        //search("ram:5t", "screensize:6.0-6.29");
        //search("ram:10t", "screensize:6.0-6.29");
        search(Arrays.asList("ram:5t","screensize:6.0-6.29"));
        //search("ram:5t", "screensize:6.0-6.29", "brand:huawei");
    }

    private void add(String tag, String... product) {
        Long add = redisTemplate.opsForSet().add(tag, product);
        log.info("标签：{} 插入商品：{}", tag, product);
    }

    private Set<String> search(List<String> otherTags) {
        //List<String> strings = Arrays.asList(otherTags);
        Set<String> diff = redisTemplate.opsForSet().difference(otherTags);
        log.info("根据标签：{} {} 检索出商品：{}", otherTags, diff);
        return diff;
    }
}
