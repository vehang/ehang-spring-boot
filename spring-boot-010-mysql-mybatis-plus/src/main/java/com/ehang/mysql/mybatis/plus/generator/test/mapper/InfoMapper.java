package com.ehang.mysql.mybatis.plus.generator.test.mapper;

import com.ehang.mysql.mybatis.plus.generator.test.domain.InfoDomain;

/**
 * @Entity com.ehang.mysql.mybatis.plus.generator.test.domain.InfoDomain
 */
public interface InfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(InfoDomain record);

    int insertSelective(InfoDomain record);

    InfoDomain selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InfoDomain record);

    int updateByPrimaryKey(InfoDomain record);

}
