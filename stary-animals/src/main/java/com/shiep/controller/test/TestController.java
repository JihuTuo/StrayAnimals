package com.shiep.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Jihu Tuo
 * @Date 2023-01-03 21:14
 **/
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        System.out.println(System.getProperty("user.dir") );
        return "succ...";
    }
}
