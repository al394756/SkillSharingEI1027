package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.RequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.modelo.OffeRequest;
import com.example.SkillSharingEI1027.modelo.Request;
import com.example.SkillSharingEI1027.modelo.Skill;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/request")
public class RequestController {
    private OffeRequestDao requestDao;
    private SkillDao skillDao;

    @Autowired
    public void setRequestDao(RequestDao requestDao,SkillDao skillDao) {
        this.requestDao = requestDao;
        this.skillDao=skillDao;
    }

    @RequestMapping("/list")
    public String listRequests(Model model){
        model.addAttribute("requests",requestDao.getActiveOffeRequests());
        return "request/list";
    }

    @RequestMapping(value = "add")
    public String addRequest(Model model){
        model.addAttribute("request",new Request());
        model.addAttribute("skills",skillDao.getSkillsName());
        return "request/add";
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("request") Request request, BindingResult bindingResult,HttpSession session){
        if (bindingResult.hasErrors())
            return "redirect:add";
        Student student=(Student) session.getAttribute("user");
        request.setStudent(student);
        request.setSkill(skillDao.getIdBySkill(request.getSkill().getIdSkill()));
        requestDao.add(request);
        return "redirect:list";
    }

    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editDescripcionSkill(Model model, @PathVariable String id,HttpSession session){
        OffeRequest request=requestDao.getOffeRequest(id);
        Student student=(Student) session.getAttribute("user");
        request.setStudent(student);
        System.out.println("id "+request.getSkill().getIdSkill());
        request.setSkill(skillDao.getSkill(request.getSkill().getIdSkill()));
        model.addAttribute("request", request);
        return "request/update";
    }

    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("request") Request request, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "request/update";
        requestDao.update(request);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String processDelete(@PathVariable String id) {
        requestDao.delete(id);
        return "redirect:../list";
    }
}
