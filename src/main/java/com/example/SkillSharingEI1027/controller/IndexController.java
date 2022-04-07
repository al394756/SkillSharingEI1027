package com.example.SkillSharingEI1027.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {
    @RequestMapping("/")
    public String indexPage(){
        return "index";
    }
}
