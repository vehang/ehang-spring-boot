package com.ehang.mysql.mybatis.plus.join;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ehang.mysql.mybatis.plus.generator.cls.demain.ClassInfo;
import com.ehang.mysql.mybatis.plus.generator.school.demain.SchoolInfo;
import com.ehang.mysql.mybatis.plus.generator.student.demain.StudentInfo;
import com.ehang.mysql.mybatis.plus.generator.student.dto.StudentInfoDTO;
import com.ehang.mysql.mybatis.plus.generator.student.service.StudentInfoService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 联表查询
 */

@SpringBootTest
@Slf4j
public class JoinTest {
    @Autowired
    StudentInfoService sutdentInfoService;

    /**
     * 联表查询单个
     */
    @Test
    public void selectJoinOne() {
        StudentInfoDTO studentInfoDTO = sutdentInfoService.selectJoinOne(StudentInfoDTO.class,
                new MPJLambdaWrapper<StudentInfo>()
                        .selectAll(StudentInfo.class)
                        .select(SchoolInfo::getSchoolName)
                        .selectAs(SchoolInfo::getSchoolAddr, StudentInfoDTO::getScAddr)
                        .select(ClassInfo::getClassName)
                        .leftJoin(SchoolInfo.class, SchoolInfo::getId, StudentInfo::getSchoolId)
                        .leftJoin(ClassInfo.class, ClassInfo::getId, StudentInfo::getClassId)
                        .eq(StudentInfo::getId, 1));
        log.info("selectJoinOne:{}", JSON.toJSONString(studentInfoDTO));
        // 等价sql：
        // SELECT t.id,t.name,t.age,t.class_id,t.school_id,t1.school_name,t1.school_addr AS scAddr,t2.class_name
        // FROM student_info t
        // LEFT JOIN school_info t1 ON (t1.id = t.school_id)
        // LEFT JOIN class_info t2 ON (t2.id = t.class_id)
        // WHERE (t.id = ?)
    }

    /**
     * 联表查询批量
     */
    @Test
    public void selectJoinList() {
        List<StudentInfoDTO> studentInfoDTOS = sutdentInfoService.selectJoinList(StudentInfoDTO.class,
                new MPJLambdaWrapper<StudentInfo>()
                        .selectAll(StudentInfo.class)
                        .select(SchoolInfo::getSchoolName)
                        .selectAs(SchoolInfo::getSchoolAddr, StudentInfoDTO::getScAddr)
                        .select(ClassInfo::getClassName)
                        .leftJoin(SchoolInfo.class, SchoolInfo::getId, StudentInfo::getSchoolId)
                        .leftJoin(ClassInfo.class, ClassInfo::getId, StudentInfo::getClassId)
                //.eq(StudentInfo::getId, 1)
        );
        log.info("selectJoinList:{}", JSON.toJSONString(studentInfoDTOS));
        // 等价sql：
        // SELECT t.id,t.name,t.age,t.class_id,t.school_id,t1.school_name,t1.school_addr AS scAddr,t2.class_name
        // FROM student_info t
        // LEFT JOIN school_info t1 ON (t1.id = t.school_id)
        // LEFT JOIN class_info t2 ON (t2.id = t.class_id)
    }

    /**
     * 分页查询
     */
    @Test
    public void selectJoinPage() {
        IPage<StudentInfoDTO> studentInfoDTOIPage = sutdentInfoService.selectJoinListPage(new Page<>(1, 2), StudentInfoDTO.class,
                new MPJLambdaWrapper<StudentInfo>()
                        .selectAll(StudentInfo.class)
                        .select(SchoolInfo::getSchoolName)
                        .selectAs(SchoolInfo::getSchoolAddr, StudentInfoDTO::getScAddr)
                        .select(ClassInfo::getClassName)
                        .leftJoin(SchoolInfo.class, SchoolInfo::getId, StudentInfo::getSchoolId)
                        .leftJoin(ClassInfo.class, ClassInfo::getId, StudentInfo::getClassId)
                        .orderByAsc(StudentInfo::getId)
        );
        log.info("selectJoinPage:{}", JSON.toJSONString(studentInfoDTOIPage));
        // 等价sql：
        // SELECT t.id,t.name,t.age,t.class_id,t.school_id,t1.school_name,t1.school_addr AS scAddr,t2.class_name
        // FROM student_info t
        // LEFT JOIN school_info t1 ON (t1.id = t.school_id)
        // LEFT JOIN class_info t2 ON (t2.id = t.class_id)
        // ORDER BY t.id ASC LIMIT 2
    }
}
