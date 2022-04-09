package com.ehang.mysql.mybatis.plus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * @author ehang
 * @title: ServiceCurdTest
 * @projectName spring-boot-010-mysql-mybatis-plus
 * @description: TODO
 * @date 2021/10/31 17:51
 */
@SpringBootTest
public class ServiceCurdTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void contextLoads() {
        UserInfo id = userInfoService.query().eq("id", 1).one();
        System.out.println(id);
    }

    @Test
    void save() {
        // 单个插入
        UserInfo userInfo = new UserInfo(1004, "张三", 10, (byte) 1);
        boolean save = userInfoService.save(userInfo);
        System.out.println("单条添加的结果：" + save);

        // 批量插入
        UserInfo lisi = new UserInfo(1005, "李四", 10, (byte) 1);
        UserInfo wangwu = new UserInfo(1006, "王五", 10, (byte) 1);
        List<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(lisi);
        userInfos.add(wangwu);
        boolean saveBatch = userInfoService.saveBatch(userInfos, 10);
        System.out.println("批量添加的结果：" + saveBatch);
    }

    @Test
    void saveOrUpdate() {
        // 单个修改
        UserInfo userInfo = new UserInfo(1004, "张三(改)", 20, (byte) 1);
        boolean saveOrUpdate = userInfoService.saveOrUpdate(userInfo);
        System.out.println("单条插入(或修改)的结果：" + saveOrUpdate);

        // 根据条件修改
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("source", 1);
        boolean saveOrUpdateByWrapper = userInfoService.saveOrUpdate(userInfo, updateWrapper);
        System.out.println("单条插入(或根据条件修改)的结果：" + saveOrUpdate);

        // 批量插入
        UserInfo lisi = new UserInfo(1005, "李四（改）", 10, (byte) 1);
        UserInfo wangwu = new UserInfo(1006, "王五（改）", 10, (byte) 1);
        List<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(lisi);
        userInfos.add(wangwu);
        boolean saveBatch = userInfoService.saveOrUpdateBatch(userInfos, 10);
        System.out.println("批量插入(或修改)的结果：" + saveBatch);
    }

    @Test
    void remove() {
        // 根据主键id删除
        boolean removeById = userInfoService.removeById(1006);
        System.out.println("根据主键ID删除用户数据：" + removeById);

        // 根据主键id批量删除
        List<Integer> ids = Arrays.asList(new Integer[]{1004, 1005, 1006});
        boolean removeByIds = userInfoService.removeByIds(ids);
        System.out.println("根据主键ids批量删除用户数据：" + removeByIds);

        save();

        // 根据条件删除
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", "张三");
        boolean remove = userInfoService.remove(queryWrapper);
        System.out.println("根据条件删除用户数据：" + remove);

        // 根据列的值删除
        Map<String, Object> cms = new HashMap();
        cms.put("user_name", "李四");
        cms.put("source", 1);
        boolean removeByMap = userInfoService.removeByMap(cms);

        System.out.println("根据字段值删除用户数据：" + removeByMap);
    }

    @Test
    public void update() {
        // 不建议使用，有
        UpdateWrapper updateWrapper = new UpdateWrapper<>();
        // 以下的setSql和set选一个即可，务必要设置条件 否则有全部修改的风险
        //updateWrapper.setSql("user_name = '张三'");
        updateWrapper.set("user_name", "王五（改1）");
        updateWrapper.eq("id", 1006);
        boolean update1 = userInfoService.update(updateWrapper);
        System.out.println("根据UpdateWrapper修改（不推荐使用）：" + update1);


        // 将符合UpdateWrapper全部修改为entity的值
        UpdateWrapper updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("user_name", "王五（改1）");
        UserInfo wangwu = new UserInfo(1006, "王五（改2）", 10, (byte) 1);
        boolean update2 = userInfoService.update(wangwu, updateWrapper1);
        System.out.println("根据UpdateWrapper修改为指定对象：" + update2);

        // 根据对象ID进行修改
        boolean update3 = userInfoService.updateById(wangwu);
        System.out.println("根据对象ID修改：" + update3);
    }
}
