package com.ehang.tools.mapstruct.mapper;

import java.util.List;

/**
 * 基础的对象转换Mapper
 *
 * @param <SOURCE> 源对象
 * @param <TARGET> 目标对象
 */
public interface BaseMapper<SOURCE, TARGET> {
    TARGET to(SOURCE var1);

    List<TARGET> to(List<SOURCE> var1);


    SOURCE from(TARGET var1);

    List<SOURCE> from(List<TARGET> var1);
}
