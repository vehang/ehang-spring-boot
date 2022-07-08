package com.ehang.tools.mapstruct.mapper;

import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.vo.UserVO2;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 嵌套对象的映射
 */
@Mapper
public interface User9Mapper extends BaseMapper<UserDTO, UserVO2> {
    User9Mapper INSTANCE = Mappers.getMapper(User9Mapper.class);

    @Mapping(source = "addressDTO.country", target = "country")
    @Override
    UserVO2 to(UserDTO userDTO);

    /**
     * 反向配置 按着to方法的配置进行反向
     * 如果不配置，country属性将会丢失，除非主动再次配置mapping
     *
     * @param var1
     * @return
     */
    @InheritInverseConfiguration(name = "to")
    @Override
    UserDTO from(UserVO2 var1);
}