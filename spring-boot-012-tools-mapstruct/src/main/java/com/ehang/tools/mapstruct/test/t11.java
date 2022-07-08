package com.ehang.tools.mapstruct.test;

import com.ehang.tools.mapstruct.dto.AddressDTO;
import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.mapper.User11Mapper;
import com.ehang.tools.mapstruct.vo.UserVO4;


/**
 * 嵌套对象的映射
 */
public class t11 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .addressDTO(AddressDTO.builder().country("中国").build())
                .build();

        UserVO4 userVO4 = User11Mapper.INSTANCE.to(userDTO);
        System.out.println(userVO4);

    }
}
