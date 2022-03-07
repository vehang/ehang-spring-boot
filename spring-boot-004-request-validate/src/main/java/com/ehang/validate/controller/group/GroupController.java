package com.ehang.validate.controller.group;

import com.ehang.validate.controller.group.dto.GroupValidateRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author ehang
 * @title: GroupController
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/4 13:08
 */
@RestController
@RequestMapping("group")
@Slf4j
public class GroupController {

    /**
     * 添加
     * 使用 GroupValidateRequestDTO.AddValidate.class 分组校验数据
     *
     * @param requestDTO
     * @return
     */
    @PostMapping
    public String add(@Validated(GroupValidateRequestDTO.AddValidate.class)
                      @RequestBody GroupValidateRequestDTO requestDTO) {
        /*
          POST请求地址：127.0.0.1:8084/group
          body数据：
            正常数据：
                {
                    "userName":"张三",
                    "passWord":"123456"
                }
            异常数据：
                {
                    "userName":"",
                    "passWord":""
                }
         */
        log.info("add requestDTO:{}", requestDTO);
        return "success";
    }

    /**
     * 修改
     * 使用 GroupValidateRequestDTO.UpdateValidate.class 分组进行数据校验
     *
     * @param requestDTO
     * @return
     */
    @PutMapping
    public String update(@Validated({GroupValidateRequestDTO.UpdateValidate.class})
                         @RequestBody GroupValidateRequestDTO requestDTO) {
         /*
          PUT请求地址：127.0.0.1:8084/group
          body数据：
            正常数据：
                {
                    "id":1,
                    "userName":"zhangsan",
                    "passWord":"123456"
                }
            异常数据：
                {
                    "userName":"zhangsan",
                    "passWord":"123456"
                }
         */
        log.info("update requestDTO:{}", requestDTO);
        return "success";
    }
}
