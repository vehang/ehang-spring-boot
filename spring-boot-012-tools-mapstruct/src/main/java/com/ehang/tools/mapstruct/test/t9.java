package com.ehang.tools.mapstruct.test;

import com.ehang.tools.mapstruct.dto.AddressDTO;
import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.mapper.User9Mapper;
import com.ehang.tools.mapstruct.vo.UserVO2;


/**
 * 嵌套对象的映射
 */
public class t9 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .addressDTO(AddressDTO.builder().country("中国").build())
                .build();

        UserVO2 userVO2 = User9Mapper.INSTANCE.to(userDTO);
        System.out.println(userVO2);

        UserDTO userDTO1 = User9Mapper.INSTANCE.from(userVO2);
        System.out.println(userDTO1);
    }
}
