package com.example.SkillSharingEI1027.controller;


import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.modelo.Skill;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

class SkillValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return Skill.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Skill skill = (Skill) obj;
        if (skill.getDescription().trim().equals("")){
            errors.rejectValue("description","compulsory","Introduce a Description");
        }
        if (skill.getName().trim().equals("")){
            errors.rejectValue("name","compulsory","Introduce a Name");
        }
    }
}
@Controller
@RequestMapping("/skill")
public class SkillController {
    private SkillDao skillDao;

    @Autowired
    public void setSkillDao(SkillDao skillDao){ this.skillDao=skillDao;}

    //Métodos para listar todas las skills
    @RequestMapping("/list")
    public String listSkills(Model model, HttpSession session){

        Student user = (Student) session.getAttribute("user");
        if (user == null || !user.isActiveAccount()){
            return "welcome";
        }
        model.addAttribute("skills", skillDao.getSkills());
        return "skill/list";
    }

    //Métodos para añadir nuevas skills
    @RequestMapping(value="/add")
    public String addSkill(Model model, HttpSession session){

        Student user = (Student) session.getAttribute("user");
        if (user == null || !user.isActiveAccount()){
            return "welcome";
        }
        model.addAttribute("skill", new Skill());
        return "skill/add";
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("skill") Skill skill, BindingResult bindingResult){
        SkillValidator skillValidator= new SkillValidator();
        skillValidator.validate(skill,bindingResult);
        if (bindingResult.hasErrors())
            return "skill/add";
        skillDao.addSkill(skill);
        return "redirect:list";
    }

    //Métodos para borrar skills
    @RequestMapping(value="/delete/{idSkill}")
    public String processDelete(@PathVariable String idSkill){
        skillDao.deleteSkill(idSkill);
        return "redirect:../list";
    }

    //Métodos para cambiar la descripción de la skill (según requisitos se supone que solo se quiere cambiar esto)
    @RequestMapping(value="/update/{idSkill}", method = RequestMethod.GET)
    public String editDescripcionSkill(Model model, @PathVariable String idSkill){
        model.addAttribute("skill", skillDao.getSkill(idSkill));
        return "skill/update";
    }

    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("skill") Skill skill, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "skill/update";
        skillDao.updateSkill(skill);
        return "redirect:list";
    }
}
