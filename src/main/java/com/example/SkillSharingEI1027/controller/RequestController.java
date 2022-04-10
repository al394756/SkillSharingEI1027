package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.RequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
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
    private RequestDao requestDao;
    private SkillDao skillDao;

    @Autowired
    public void setRequestDao(RequestDao requestDao,SkillDao skillDao) {
        this.requestDao = requestDao;
        this.skillDao=skillDao;
    }

    @RequestMapping("/list")
    public String listRequests(Model model){
        model.addAttribute("requests",requestDao.getActiveRequests());
        return "request/list";
    }

    @RequestMapping(value = "add")
    public String addRequest(Model model){
        model.addAttribute("request",new Request());
        List<String> skills=new ArrayList<>();
        for (Skill skill:skillDao.getSkills())
            skills.add(skill.getName());
        model.addAttribute("skills",skills);
        return "request/add";
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("request") Request request, BindingResult bindingResult,HttpSession session){
        if (bindingResult.hasErrors())
            return "redirect:add";
        Student student=(Student) session.getAttribute("user");
        request.setIdStudent(student.getIdStudent());
        request.setIdSkill(skillDao.getId(request.getIdSkill()));
        requestDao.addRequest(request);
        return "redirect:list";
    }

    @RequestMapping(value="/update/{idRequest}", method = RequestMethod.GET)
    public String editDescripcionSkill(Model model, @PathVariable String idRequest){
        model.addAttribute("request", requestDao.getRequest(idRequest));
        return "request/update";
    }

    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("request") Request request, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "request/update";
        requestDao.updateRequest(request);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idRequest}")
    public String processDelete(@PathVariable String idRequest) {
        requestDao.deleteRequest(idRequest);
        return "redirect:../list";
    }
}
