package com.example.SkillSharingEI1027.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
@RequestMapping("/")
public class IndexController {
    @RequestMapping("/")
    public String indexPage(HttpSession session){
        session.setAttribute("today", LocalDate.now());
        return "welcome";
    }
}
