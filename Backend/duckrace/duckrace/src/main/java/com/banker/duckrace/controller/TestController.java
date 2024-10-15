package com.banker.duckrace.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/test")
public class TestController {

    @GetMapping("/")
    public String all() {
        return "Hello World!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

}
