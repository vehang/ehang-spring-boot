package com.ehang.tools.mapstruct.test;

import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.mapper.User8Mapper;
import com.ehang.tools.mapstruct.vo.UserVO1;

import java.math.BigDecimal;
import java.util.Date;


/**
 * BigDecimal转换测试
 */
public class t8 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .updateTime(new Date())
                .wallet(10000.45678)
                .deposit(new BigDecimal(10000000.324))
                .build();

        UserVO1 userVO1 = User8Mapper.INSTANCE.to(userDTO);
        System.out.println(userVO1);

        UserDTO userDTO1 = User8Mapper.INSTANCE.from(userVO1);
        System.out.println(userDTO1);
    }
}
