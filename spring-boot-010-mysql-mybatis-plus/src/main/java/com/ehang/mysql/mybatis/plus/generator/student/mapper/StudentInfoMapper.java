package com.ehang.mysql.mybatis.plus.generator.student.mapper;

import com.ehang.mysql.mybatis.plus.generator.student.demain.StudentInfo;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.MapKey;

import java.util.HashMap;
import java.util.List;

/**
 * @Entity com.ehang.mysql.mybatis.plus.generator.student.demain.StudentInfo
 */
public interface StudentInfoMapper extends MPJBaseMapper<StudentInfo> {

    @MapKey("student_id")
    HashMap<String, Object> getStudentMap();

    List<StudentInfo> getStudentList();
}




