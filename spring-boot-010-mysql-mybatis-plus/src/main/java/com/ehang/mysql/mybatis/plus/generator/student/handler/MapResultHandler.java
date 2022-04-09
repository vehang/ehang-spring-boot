package com.ehang.mysql.mybatis.plus.generator.student.handler;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ehang
 * @title: MapResultHandler
 * @projectName spring-boot-010-mysql-mybatis-plus
 * @description: TODO
 * @date 2021/12/11 14:18
 */
public class MapResultHandler implements ResultHandler {
    private final Map mappedResults = new HashMap();

    @Override
    public void handleResult(ResultContext resultContext) {
        Map map = (Map) resultContext.getResultObject();
        // xml配置中通过resultMap配置的property
        mappedResults.put(map.get("key"), map.get("value"));
    }

    public Map getMappedResults() {
        return mappedResults;
    }
}
