package com.example.SkillSharingEI1027.controller;


import com.example.SkillSharingEI1027.dao.SkillDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/skill")
public class SkillController {
    private SkillDao skillDao;

    @Autowired
    public void setSkillDao(SkillDao skillDao){ this.skillDao=skillDao;}

    @RequestMapping("/list")
    public String listSkills(Model model){
        model.addAttribute("skills", skillDao.getSkills());
        return "skill/list";
    }
}
