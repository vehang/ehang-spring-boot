package com.ehang.tools.mapstruct.mapper;

import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.vo.UserVO1;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 自定义不同日期格式转换映射器
 * uses = DateMapper.class
 */
@Mapper(uses = {
        DateMapper1.class,
        DateMapper2.class
})
public interface User6Mapper extends BaseMapper<UserDTO, UserVO1> {
    User6Mapper INSTANCE = Mappers.getMapper(User6Mapper.class);

    @Mappings({
            @Mapping(source = "createTime", target = "createTime", qualifiedByName = {"dateMapper1"}),
            @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = {"dateMapper2"})
    })
    @Override
    UserVO1 to(UserDTO var1);
}