package com.ehang.validate.controller.ip;

import com.ehang.validate.geoip.IpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LENOVO
 * @title: IpController
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/5/15 20:16
 */
@RestController
public class IpController {
    @GetMapping("/user/ip")
    public String userIp(HttpServletRequest request) {
        return IpUtils.getIpAddr(request);
    }
}
