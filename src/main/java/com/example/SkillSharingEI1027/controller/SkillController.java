package com.example.SkillSharingEI1027.controller;


import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.modelo.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/skill")
public class SkillController {
    private SkillDao skillDao;

    @Autowired
    public void setSkillDao(SkillDao skillDao){ this.skillDao=skillDao;}

    //Métodos para listar todas las skills
    @RequestMapping("/list")
    public String listSkills(Model model){
        model.addAttribute("skills", skillDao.getSkills());
        return "skill/list";
    }

    //Métodos para añadir nuevas skills
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
