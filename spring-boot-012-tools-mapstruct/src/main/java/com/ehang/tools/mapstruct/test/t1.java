package com.ehang.tools.mapstruct.test;

import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.mapper.User1Mapper;
import com.ehang.tools.mapstruct.vo.UserVO1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 最基本的类型转换，无需做任何特殊处理的情况
 */
public class t1 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .build();

        UserVO1 userVO1 = User1Mapper.INSTANCE.to(userDTO);
        System.out.println(userVO1);

        List<UserDTO> userDTOS = new ArrayList<>();
        userDTOS.add(userDTO);
        List<UserVO1> userVO1s = User1Mapper.INSTANCE.to(userDTOS);
        System.out.println(userVO1s);
    }
}
