package com.ehang.tools.mapstruct.mapper;

import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.vo.UserVO1;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数值类型格式化
 */
@Mapper
public interface User7Mapper extends BaseMapper<UserDTO, UserVO1> {
    User7Mapper INSTANCE = Mappers.getMapper(User7Mapper.class);

    @Mapping(source = "wallet", target = "wallet", numberFormat = "$#.00")
    @Override
    UserVO1 to(UserDTO var1);

    @Mapping(source = "wallet", target = "wallet", numberFormat = "$#.00")
    @Override
    UserDTO from(UserVO1 var1);

    @IterableMapping(numberFormat = "$#.00")
    List<String> doubleList2String(List<Double> vas);

    @IterableMapping(numberFormat = "$#.00")
    List<Double> stringList2Double(List<String> vas);
}