package com.ehang.validate.controller.custom;

import com.alibaba.fastjson.JSON;
import com.ehang.validate.controller.custom.dto.CustomRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义校验规则
 *
 * @author ehang
 * @title: CustomController
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/4 14:01
 */
@RestController
@Slf4j
@RequestMapping("custom")
public class CustomController {

    @PostMapping
    public String add(@Validated @RequestBody CustomRequestDTO requestDTO) {
        /*
          POST请求地址：127.0.0.1:8084/custom
          body数据：
            正常情况：
                {
                    "userName":"ABC",
                    "nickName":"abc"
                }
            异常情况：
                {
                    "userName":"ABc",
                    "nickName":"Abc"
                }

         */

        log.info("add requestDTO:{}", JSON.toJSONString(requestDTO));

        return "success";
    }
}
