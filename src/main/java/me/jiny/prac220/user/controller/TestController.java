package me.jiny.prac220.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/test")
    public String test() {
        return "springboot + react connected!";
    }

    @GetMapping("/")
    public String index() {
        return "home";
    }
}
