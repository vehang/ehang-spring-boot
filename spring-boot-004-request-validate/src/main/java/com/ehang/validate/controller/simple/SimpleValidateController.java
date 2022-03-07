package com.ehang.validate.controller.simple;

import com.alibaba.fastjson.JSON;
import com.ehang.validate.controller.simple.dto.UserRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ehang
 * @title: SimpleValidateController
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/4 12:50
 */
@RestController
@RequestMapping("simple")
@Slf4j
public class SimpleValidateController {

    @PostMapping()
    public void add(@Validated @RequestBody UserRequestDTO userRequestDTO) {
        /*
            请求 url：127.0.0.1:8084/simple
            请求body：
                {
                    "userName":"张三",
                    "passWord":"123456",
                    "age":80,
                    "phoneNum":"18000000001"
                }
         */
        log.info("userRequestDTO:{}", JSON.toJSONString(userRequestDTO));
    }
}
