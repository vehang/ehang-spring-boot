package com.ehang.config.value.spel;

import java.util.Random;

/**
 * @author LENOVO
 * @title: RandomUtil
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/8 17:33
 */
public class RandomUtil {
    public static Integer num() {
        Random random = new Random();
        return random.nextInt(10000);
    }

    public static String nickName(String name) {
        Random random = new Random();
        return name + "_" + random.nextInt(10000);
    }
}
