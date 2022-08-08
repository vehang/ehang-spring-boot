package com.ehang.redis.geo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author 一行Java
 * @title: GEOTest
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/28 17:29
 */
@SpringBootTest
@Slf4j
public class GEOTest {
    private final String KEY = "geo:drinks";

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test() {
        add("starbucks", new Point(116.62445, 39.86206));
        add("yidiandian", new Point(117.3514785, 38.7501247));
        add("xicha", new Point(116.538542, 39.75412));

        get("starbucks", "yidiandian", "xicha");

        GeoResults nearByXY = getNearByXY(new Point(116, 39), new Distance(120, Metrics.KILOMETERS));
        List<GeoResult> content = nearByXY.getContent();
        for (GeoResult geoResult : content) {
            log.info("{}", geoResult.getContent());
        }

        GeoResults nearByPlace = getNearByPlace("starbucks", new Distance(120, Metrics.KILOMETERS));
        content = nearByPlace.getContent();
        for (GeoResult geoResult : content) {
            log.info("{}", geoResult.getContent());
        }

        getGeoHash("starbucks", "yidiandian", "xicha");

        del("yidiandian", "xicha");
    }

    private void add(String name, Point point) {
        Long add = redisTemplate.opsForGeo().add(KEY, point, name);
        log.info("成功添加名称：{} 的坐标信息信息：{}", name, point);
    }


    private void get(String... names) {
        List<Point> position = redisTemplate.opsForGeo().position(KEY, names);
        log.info("获取名称为：{} 的坐标信息：{}", names, position);
    }

    private void del(String... names) {
        Long remove = redisTemplate.opsForGeo().remove(KEY, names);
        log.info("删除名称为：{} 的坐标信息数量：{}", names, remove);
    }

    /**
     * 根据坐标 获取指定范围的位置
     *
     * @param point
     * @param distance
     * @return
     */
    private GeoResults getNearByXY(Point point, Distance distance) {
        Circle circle = new Circle(point, distance);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.
                newGeoRadiusArgs().
                includeDistance(). // 包含距离
                includeCoordinates(). // 包含坐标
                sortAscending(). // 排序 还可选sortDescending()
                limit(5); // 获取前多少个
        GeoResults geoResults = redisTemplate.opsForGeo().radius(KEY, circle, args);
        log.info("根据坐标获取：{} {} 范围的数据：{}", point, distance, geoResults);
        return geoResults;
    }

    /**
     * 根据一个位置，获取指定范围内的其他位置
     *
     * @param name
     * @param distance
     * @return
     */
    private GeoResults getNearByPlace(String name, Distance distance) {
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.
                newGeoRadiusArgs().
                includeDistance(). // 包含距离
                includeCoordinates(). // 包含坐标
                sortAscending(). // 排序 还可选sortDescending()
                limit(5); // 获取前多少个
        GeoResults geoResults = redisTemplate.opsForGeo()
                .radius(KEY, name, distance, args);
        log.info("根据位置：{} 获取： {} 范围的数据：{}", name, distance, geoResults);
        return geoResults;
    }

    /**
     * 获取GEO HASH
     *
     * @param names
     * @return
     */
    private List<String> getGeoHash(String... names) {
        List<String> hash = redisTemplate.opsForGeo().hash(KEY, names);
        log.info("names：{} 对应的hash：{}", names, hash);
        return hash;
    }
}
