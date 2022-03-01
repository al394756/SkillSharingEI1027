package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.RequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.modelo.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/request")
public class RequestController {
    private RequestDao requestDao;

    @Autowired
    public void setRequestDao(RequestDao requestDao) {
        this.requestDao = requestDao;
    }

    @RequestMapping("/list")
    public String listRequests(Model model){
        model.addAttribute("requests",requestDao.getRequests());
        return "request/list";
    }

    @RequestMapping("/skills")
    public String listSkills(Model model) {
        SkillDao skillDao=new SkillDao();
        model.addAttribute("skills",skillDao.getSkills());
        return "request/add";
    }

    @RequestMapping(value = "add")
    public String addRequest(Model model){
        model.addAttribute("request",new Request());
        return "request/add";
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("request") Request request, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "request/add";
        requestDao.addRequest(request);
        return "redirect:list";
    }

    @RequestMapping(value="/update/{idRequest}", method = RequestMethod.GET)
    public String editDescripcionSkill(Model model, @PathVariable String idRequest){
        model.addAttribute("skill", requestDao.getRequest(idRequest));
        return "request/update";
    }

    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String procssUpdateSubmit(@ModelAttribute("request") Request request, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "request/update";
        requestDao.updateRequest(request);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idRequest}")
    public String processDeleteClassif(@PathVariable String idRequest) {
        requestDao.cancelRequest(idRequest);
        return "redirect:../../list";
    }
}
