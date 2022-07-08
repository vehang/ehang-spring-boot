package com.ehang.tools.mapstruct.mapper;

import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<UserDTO, UserVO> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
