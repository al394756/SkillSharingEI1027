package com.example.SkillSharingEI1027.controller;


import com.example.SkillSharingEI1027.dao.CollaborationDao;
import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.modelo.Collaboration;
import com.example.SkillSharingEI1027.modelo.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/collaboration")
public class CollaborationController {
    private CollaborationDao collaborationDao;

    @Autowired
    public void setCollaborationDao(CollaborationDao collaborationDao){
        this.collaborationDao = collaborationDao;
    }

    @RequestMapping("/list")
    public String listCollaborations(Model model){
        model.addAttribute("collaborations",collaborationDao.getCollaborations());
        return "collaboration/list"; //falta html
    }

    @RequestMapping(value = "add")
    public String addCollaboration(Model model){
        model.addAttribute("collaboration",new Collaboration());
        List<String> offers = new ArrayList<>();
        for (Skill skill : OffeRequestMethods.list)
            skills.add(skill.getName());
        model.addAttribute("skills", skills);
        return "collaboration/add"; //falta html
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("request") Collaboration collaboration, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "collaboration/add";
        collaborationDao.addCollaboration(collaboration);
        return "redirect:list";
    }

    @RequestMapping(value="/update/{idCollaboration}", method = RequestMethod.GET)
    public String editCollaboration(Model model, @PathVariable String idCollaboration){
        model.addAttribute("collaboration", collaborationDao.getCollaboration(idCollaboration));
        return "collaboration/update"; //falta html
    }

    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("collaboration") Collaboration collaboration, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "collaboration/update"; //falta html
        collaborationDao.updateCollaboration(collaboration);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idCollaboration}")
    public String processDelete(@PathVariable String idCollaboration) {
        collaborationDao.deleteCollaboration(idCollaboration);
        return "redirect:../list"; //falta html
    }










}
