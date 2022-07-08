package com.ehang.tools.mapstruct.mapper;

import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.vo.UserVO4;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * expression表达式 调用其他方法进行转换，方法可以自定义
 */
@Mapper
public interface User11Mapper {
    User11Mapper INSTANCE = Mappers.getMapper(User11Mapper.class);

    @Mapping(target = "addrJson", expression = "java(com.alibaba.fastjson.JSON.toJSONString(dto.getAddressDTO()))")
    UserVO4 to(UserDTO dto);
}