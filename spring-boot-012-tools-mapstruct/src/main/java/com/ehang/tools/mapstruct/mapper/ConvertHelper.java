package com.ehang.tools.mapstruct.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ehang
 * @title: ConvertHelper
 * @projectName ehang-springboot-012-tools-mapstruct
 * @description: TODO
 * @date 2021/6/11 12:38
 */
public class ConvertHelper {
    List<String> getList2Obj(List<Object> objs) {
//        if (null != objs && objs.size() > i) {
//            return objs.get(i);
//        }
        List<String> strs = new ArrayList<>();
        for (Object obj : objs) {
            strs.add(obj.toString());
        }

        return strs;
    }
}
