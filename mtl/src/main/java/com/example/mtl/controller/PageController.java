package com.example.mtl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/10/26 11:06
 */
@Controller
public class PageController {

    @RequestMapping("/")
    public String defaultPath() {
        return "index";
    }

    @RequestMapping("/index.html")
    public String index() {
        return "index";
    }

}
