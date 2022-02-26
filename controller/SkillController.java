package com.example.SkillSharingEI1027.controller;


import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.modelo.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value="/add")
    public String addSkill(Model model){
        model.addAttribute("skill", new Skill());
        return "skill/add";
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("skill") Skill skill, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "skill/add";
        skillDao.addSkill(skill);
        return "redirect:list";
    }
}
