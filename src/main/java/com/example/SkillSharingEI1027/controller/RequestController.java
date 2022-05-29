package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.Request;
import com.example.SkillSharingEI1027.modelo.Student;
import com.example.SkillSharingEI1027.services.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
@Controller
@RequestMapping("/request")
public class RequestController {
    private final OffeRequestMethods<Request> offeRequestMethods=new OffeRequestMethods<>();
    private final String type="Request";

    @Autowired
    public void setOffeRequestDao(OffeRequestDao offeRequestDao, SkillDao skillDao, CollaborationService collaborationService) {
        offeRequestMethods.setOffeRequestDao(offeRequestDao,skillDao, collaborationService);
    }

    @RequestMapping("/list")
    public String listRequests(Model model, HttpSession session){
        Student user = (Student) session.getAttribute("user");
        if (user!=null && !user.isActiveAccount()){
            return "welcome";
        }
        session.setAttribute("backUrl","/request/list");

        return offeRequestMethods.list(model,type,session);
    }

    @RequestMapping("/list/idSkill")
    public String listFiltered(Model model, HttpSession session, @RequestParam String idSkill){
        Student user = (Student) session.getAttribute("user");
        if (user!=null && !user.isActiveAccount()){
            return "welcome";
        }
        session.setAttribute("backUrl","/request/list");

        return offeRequestMethods.listFiltered(model,"Request",idSkill,session,"request");
    }

    @RequestMapping(value = "/add")
    public String addRequest(Model model,HttpSession session){
        Student user = (Student) session.getAttribute("user");

        if (user == null || !user.isActiveAccount()){
            return "welcome";
        }
        model.addAttribute("offeRequest",new Request());
        return offeRequestMethods.add(model,type,session);
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("request") Request request, BindingResult bindingResult,HttpSession session,Model model){
        return offeRequestMethods.processAddSubmit(request,bindingResult,session,model);
    }

    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable String id, HttpSession session){
        return offeRequestMethods.edit(model, id,type,session);
    }

    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("request") Request request, BindingResult bindingResult, HttpSession session, Model model){
        return offeRequestMethods.processUpdateSubmit(request,bindingResult, session, model);
    }

    @RequestMapping(value = "/cancel/{id}")
    public String processDelete(@PathVariable String id, HttpSession session) {
        return offeRequestMethods.processDelete(id, session);
    }

    @RequestMapping(value="/listexisting",method = RequestMethod.POST)
    public String processConfirmAddSubmit(HttpSession session){
        return offeRequestMethods.processConfirmAddSubmit(session);
    }

    @RequestMapping(value = "/listexisting")
    public String listExisting(HttpSession session,Model model){
        return offeRequestMethods.listExisting(session,model);
    }
}
