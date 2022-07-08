package com.ehang.tools.mapstruct.mapper;


import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.vo.UserVO3;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface User3Mapper extends BaseMapper<UserDTO, UserVO3> {
    User3Mapper INSTANCE = Mappers.getMapper(User3Mapper.class);

    @Mapping(source = "name", target = "nickName")
    @Override
    UserVO3 to(UserDTO var1);

    // name为 A==>B 的方法名
    @InheritInverseConfiguration(name = "to")
    @Override
    UserDTO from(UserVO3 var1);
}
