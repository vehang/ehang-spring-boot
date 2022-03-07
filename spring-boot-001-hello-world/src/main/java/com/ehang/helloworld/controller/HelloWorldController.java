package com.ehang.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class HelloWorldController {

    @GetMapping("/")
    public String hello() {
        return "Hello java！Hello SpringBoot！Hello World";
    }
}
