package com.ehang.tools.mapstruct.test;

import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.mapper.User4Mapper;
import com.ehang.tools.mapstruct.vo.UserVO1;

import java.util.Date;


/**
 * 忽略指定字段的映射
 */
public class t4 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .build();

        UserVO1 userVO1 = User4Mapper.INSTANCE.to(userDTO);
        System.out.println(userVO1);
    }
}
