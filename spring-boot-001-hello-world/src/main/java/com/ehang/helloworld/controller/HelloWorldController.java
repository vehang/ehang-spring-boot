package com.ehang.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公众号：一行Java
 */
@RestController
public class HelloWorldController {

    @GetMapping("/")
    public String hello() {
        return "Hello java！Hello SpringBoot！Hello World";
    }
}
