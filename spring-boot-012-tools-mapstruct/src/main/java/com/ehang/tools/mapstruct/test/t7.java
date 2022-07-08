package com.ehang.tools.mapstruct.test;

import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.mapper.User7Mapper;
import com.ehang.tools.mapstruct.vo.UserVO1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 数值类型转换格式化
 */
public class t7 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .updateTime(new Date())
                .wallet(10000.45678)
                .build();

        UserVO1 userVO1 = User7Mapper.INSTANCE.to(userDTO);
        System.out.println(userVO1);

        UserDTO userDTO1 = User7Mapper.INSTANCE.from(userVO1);
        System.out.println(userDTO1);

        List<Double> vas = new ArrayList<>();
        vas.add(123.5585);
        vas.add(784.1565488);
        vas.add(12.11243);
        // string list转 double
        List<String> strings = User7Mapper.INSTANCE.doubleList2String(vas);
        System.out.println(strings);

        // double list 转 string
        List<Double> doubles = User7Mapper.INSTANCE.stringList2Double(strings);
        System.out.println(doubles);
    }
}
