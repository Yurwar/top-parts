package com.topparts;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
    @GetMapping("/hello")
    public String getResponse() {
        return "Hello world";
    }
}
