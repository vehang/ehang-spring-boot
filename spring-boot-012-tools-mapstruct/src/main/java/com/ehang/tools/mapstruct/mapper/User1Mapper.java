package com.ehang.tools.mapstruct.mapper;

import com.ehang.tools.mapstruct.dto.UserDTO;
import com.ehang.tools.mapstruct.vo.UserVO1;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 简单转换
 *
 * @author ehang
 * @title: User1Mapper
 * @projectName ehang-springboot-012-tools-mapstruct
 * @description: TODO
 * @date 2021/6/9 23:13
 */
@Mapper
public interface User1Mapper extends BaseMapper<UserDTO, UserVO1> {
    User1Mapper INSTANCE = Mappers.getMapper(User1Mapper.class);

//    @Mappings({
//            日期格式化
//            @Mapping(source = "createTime",target = "createTime",dateFormat = "yyyyMMdd")
//    })
//    @Override
//    UserVO1 to(UserDTO var1);
}
