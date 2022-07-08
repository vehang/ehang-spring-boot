package com.ehang.tools.mapstruct.test;

import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.vo.UserVO;
import com.ehang.tools.mapstruct.vo.UserVO1;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 基于BeanUtils的拷贝
 */
public class t0 {
    public static void main(String[] args) {
        UserDTO userDTO = UserDTO.builder()
                .name("张三")
                .age(10)
                .createTime(new Date())
                .build();

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO, userVO);
        System.out.println(userVO);

        // 测试属性名相同，但类型不同的情况
        UserVO1 userVO1 = new UserVO1();
        BeanUtils.copyProperties(userDTO, userVO1);
        System.out.println(userVO1);
    }
}
