package com.ehang.mysql.mybatis.plus.student;

import com.alibaba.fastjson.JSON;
import com.ehang.mysql.mybatis.plus.generator.student.demain.StudentInfo;
import com.ehang.mysql.mybatis.plus.generator.student.mapper.StudentInfoMapper;
import com.ehang.mysql.mybatis.plus.generator.student.service.StudentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

/**
 * @author ehang
 * @title: StudentTest
 * @projectName spring-boot-010-mysql-mybatis-plus
 * @description: TODO
 * @date 2021/12/11 11:39
 */
@SpringBootTest
@Slf4j
public class StudentTest {

    @Autowired
    StudentInfoService sutdentInfoService;

    @Autowired
    StudentInfoMapper studentInfoMapper;

    @Test
    void map() {
        HashMap<String, Object> studentId = studentInfoMapper.getStudentMap();
        studentId.forEach((id, studentInfo) -> log.info("学号：{} 信息：{}", id, JSON.toJSONString(studentInfo)));
    }

    @Test
    void list() {
        List<StudentInfo> studentList = studentInfoMapper.getStudentList();
        studentList.forEach(studentInfo -> log.info("{}", JSON.toJSONString(studentInfo)));
    }
}
