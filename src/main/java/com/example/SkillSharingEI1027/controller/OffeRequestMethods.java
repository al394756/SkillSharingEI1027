package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.*;
import com.example.SkillSharingEI1027.services.CollaborationService;
import com.example.SkillSharingEI1027.services.CollaborationSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class OffeRequestMethods <T> {
    private OffeRequestDao offeRequestDao;
    private SkillDao skillDao;
    private StudentDao studentDao;
    private CollaborationService collaborationService;

    @Autowired
    public void setCollaborationService(CollaborationService collaborationService) {
        this.collaborationService = collaborationService;
    }

    public void setOffeRequestDao(OffeRequestDao offeRequestDao, SkillDao skillDao, StudentDao studentDao) {
        this.offeRequestDao = offeRequestDao;
        this.skillDao = skillDao;
        this.studentDao=studentDao;
    }

    public String list(Model model, String type, HttpSession session) {
        model.addAttribute("type",type);
        List<OffeRequest> lista = offeRequestDao.getActiveOffeRequests(type);
        //List<OffeRequest> lista2 = collaborationService.giveOffeRequestPendientes(type,(Student) session.getAttribute("session"));
        //lista.removeAll(lista2);
        for (OffeRequest r : lista){
            r.setSkill(skillDao.getSkill(r.getSkill().getIdSkill()));
            r.setStudent(studentDao.getStudentUsingId(r.getStudent().getIdStudent()));
        }
        model.addAttribute("list", lista);
        return "offeRequest/list";
    }

    public String add(Model model,String type, HttpSession session) {
        model.addAttribute("type",type);
        session.setAttribute("type",type);
        List<String> skills = new ArrayList<>();
        skills.add("Select one option");
        for (Skill skill : skillDao.getSkills())
            skills.add(skill.getName());
        model.addAttribute("skills", skills);
        return "offeRequest/add";
    }

    public String processAddSubmit(OffeRequest offeRequest, BindingResult bindingResult, HttpSession session, Model model) {
        OffeRequestValidator offeRequestValidator=new OffeRequestValidator();
        offeRequestValidator.validate(offeRequest,bindingResult);
        if (bindingResult.hasErrors())
            return "redirect:/"+offeRequest.getUrl()+"/add";

        offeRequest.setStudent((Student) session.getAttribute("user"));

        Skill skill=skillDao.getIdBySkill(offeRequest.getSkill().getIdSkill());
        offeRequest.setSkill(skill);

        String type= (String) session.getAttribute("type");
        model.addAttribute("type2",type);
        if (type.equals("Offer")) type="Request";
        else type="Offer";
        List<OffeRequest> offeRequestList=offeRequestDao.getOffeRequestWithSkill(type,skill.getIdSkill(),offeRequest.getStartDate());
        if (offeRequestList.isEmpty()){
            offeRequestDao.add(offeRequest);
            return "redirect:list";
        }
        String t = "Offer";
        if (type.equals(t))
            t = "Request";
        List<OffeRequest> listaParaMail = offeRequestDao.getOffeRequestWithSkill(t,skill.getIdSkill(),offeRequest.getStartDate());
        System.out.println(listaParaMail);
        model.addAttribute("mailTosend", listaParaMail);
        model.addAttribute("type",type);
        session.removeAttribute("type");
        model.addAttribute("skill",skill);
        model.addAttribute("list",offeRequestList);
        session.setAttribute("offeRequest",offeRequest);
        model.addAttribute("offeRequest",offeRequest);
        return "offeRequest/listexisting";
    }

    public String processConfirmAddSubmit(HttpSession session){
        OffeRequest offeRequest=(OffeRequest)session.getAttribute("offeRequest");
        session.removeAttribute("offeRequest");
        offeRequestDao.add(offeRequest);
        return "redirect:list";
    }

    public String edit(Model model,String id,String type, HttpSession session){
        model.addAttribute("type",type);
        OffeRequest offeRequest= offeRequestDao.getOffeRequest(id);
        offeRequest.setSkill(skillDao.getSkill(offeRequest.getSkill().getIdSkill()));
        offeRequest.setStudent(studentDao.getStudentUsingId(offeRequest.getStudent().getIdStudent()));
        OffeRequest offeRequestCopia;
        if (type.equals("Request"))
            offeRequestCopia= new Request(offeRequest);
        else
            offeRequestCopia=new Offer(offeRequest);

        session.setAttribute("skill",offeRequestCopia.getSkill());
        session.setAttribute("offeRequest",offeRequestCopia);
        model.addAttribute("offeRequest",offeRequest);
        return "offeRequest/update";
    }

    public String processUpdateSubmit(OffeRequest offeRequest, BindingResult bindingResult, HttpSession session){
        OffeRequest offeRequestAntigua = (OffeRequest) session.getAttribute("offeRequest");
        Skill skill = (Skill) session.getAttribute("skill");
        OffeRequestValidator offeRequestValidator=new OffeRequestValidator();

        String descripcion = offeRequest.getDescription();
        offeRequest.setId(offeRequestAntigua.getId());
        skill.setDescription(descripcion);
        offeRequest.setStudent(offeRequestAntigua.getStudent());
        offeRequest.setSkill(skill);
        offeRequest.setStudent(offeRequest.getStudent());
        session.removeAttribute("offeRequest");
        session.removeAttribute("skill");

        offeRequestValidator.validateUpdate(offeRequest,bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/"+offeRequest.getUrl() + "/update/"+offeRequest.getId();
        }
        offeRequestDao.update(offeRequest);
        return "redirect:list";
    }

    public String processDelete(String idOffer) {
        offeRequestDao.delete(idOffer);
        return "redirect:../list";
    }
}

class OffeRequestValidator extends OffeRequestMethods implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return OffeRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        OffeRequest offeRequest=(OffeRequest) obj;
        if (offeRequest.getSkill().getIdSkill().trim().equals("Select one option"))
            errors.rejectValue("skill.idSkill", "compulsory", "Introduce a valid Skill");
        if (offeRequest.getStartDate()==null)
            errors.rejectValue("startDate","compulsory","Introduce a date");
        if (offeRequest.getEndDate()==null)
            errors.rejectValue("endDate","compulsory","Introduce a date");
        else if (offeRequest.getStartDate()!=null&&offeRequest.getStartDate().compareTo(offeRequest.getEndDate())>0)
            errors.rejectValue("endDate","compulsory","End Date must be bigger than Start Date");
        if (offeRequest.getSkill().getIdSkill().trim().equals(""))
            errors.rejectValue("description", "compulsory", "Introduce a description");
    }

    public void validateUpdate(Object obj, Errors errors) {
        OffeRequest offeRequest=(OffeRequest) obj;
        if (offeRequest.getStartDate()==null)
            errors.rejectValue("startDate","compulsory","Introduce a date");
        if (offeRequest.getEndDate()==null)
            errors.rejectValue("endDate","compulsory","Introduce a date");
        else if (offeRequest.getStartDate()!=null&&offeRequest.getStartDate().compareTo(offeRequest.getEndDate())>0)
            errors.rejectValue("endDate","compulsory","End Date must be bigger than Start Date");
        if (offeRequest.getDescription().trim().equals(""))
            errors.rejectValue("description", "compulsory", "Introduce a description");
    }
}
