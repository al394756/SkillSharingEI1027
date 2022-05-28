package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.*;
import com.example.SkillSharingEI1027.services.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.servlet.http.HttpSession;
import java.util.*;


public class OffeRequestMethods <T> {
    private OffeRequestDao offeRequestDao;
    private SkillDao skillDao;
    private CollaborationService collaborationService;


    public void setOffeRequestDao(OffeRequestDao offeRequestDao, SkillDao skillDao,  CollaborationService collaborationService) {
        this.offeRequestDao = offeRequestDao;
        this.skillDao = skillDao;
        this.collaborationService= collaborationService;
    }

    public String list(Model model, String type, HttpSession session) {
        Student student = (Student) session.getAttribute("user");
        model.addAttribute("type",type);
        String url;
        if (type.equals("Offer")) url="offer";
        else url="request";
        List<OffeRequest> lista;
        if (student == null)
            lista = offeRequestDao.getActiveOffeRequests(type);
        else lista  = collaborationService.giveOffeRequestNoAceptadas(student,type);
        model.addAttribute("skills", skillDao.getSkills());
        model.addAttribute("list", lista);
        model.addAttribute("url",url);
        return "offeRequest/list";
    }

    public String listFiltered(Model model, String type,String idSkill, HttpSession session,String url) {
        Student student = (Student) session.getAttribute("user");
        model.addAttribute("type",type);
        List<OffeRequest> lista;
        if (student == null){
            if (idSkill.equals("all")) lista=offeRequestDao.getActiveOffeRequests(type);
            else lista = offeRequestDao.getOffeRequestFiltered(type,idSkill);
        }
        else {
            if (idSkill.equals("all")) lista=collaborationService.giveOffeRequestNoAceptadas(student,type);
            else lista  = collaborationService.giveOffeRequestNoAceptadas(student,type,idSkill);
        }
        model.addAttribute("skills", skillDao.getSkills());
        model.addAttribute("list", lista);
        model.addAttribute("url",url);
        return "offeRequest/list";
    }

    public String add(Model model,String type, HttpSession session) {
        model.addAttribute("type",type);
        session.setAttribute("type",type);
        model.addAttribute("skills", skillDao.getSkills());
        return "offeRequest/add";
    }

    public String processAddSubmit(OffeRequest offeRequest, BindingResult bindingResult, HttpSession session, Model model) {
        OffeRequestValidator offeRequestValidator=new OffeRequestValidator();
        offeRequestValidator.validate(offeRequest,bindingResult);
        if (bindingResult.hasErrors())
            return "redirect:/" + offeRequest.getUrl() + "/add";

        offeRequest.setStudent((Student) session.getAttribute("user"));

        String type2= (String) session.getAttribute("type");
        session.setAttribute("type2",type2);
        String type;
        if (type2.equals("Offer")) type="Request";
        else type="Offer";
        session.setAttribute("type",type);
        List<OffeRequest> offeRequestList=offeRequestDao.getOffeRequestWithSkill(type,offeRequest.getSkill().getIdSkill(),offeRequest.getStartDate(), offeRequest.getStudent());
        if (offeRequestList.isEmpty()){
            offeRequestDao.add(offeRequest);
            session.setAttribute("correcto",true);
            return "redirect:list";
        }
        session.setAttribute("skill",offeRequest.getSkill());
        session.setAttribute("offeRequest",offeRequest);
        session.setAttribute("offeRequestList",offeRequestList);




        return "redirect:/"+type2.toLowerCase(Locale.ROOT)+"/listexisting";
    }

    public String listExisting(HttpSession session, Model model){
        List<String> setMail = new LinkedList<>();
        int count = 0;
        List<OffeRequest> offeRequestList= (List<OffeRequest>) session.getAttribute("offeRequestList");
        for (OffeRequest or: offeRequestList ) {
            if (! setMail.contains(or.getStudent().getEmail())) {
                if (count<3) {
                    setMail.add(or.getStudent().getEmail());
                    count--;
                }
                count++;
            }


        }
        if (count > 0)
            setMail.add(" and" + count + "more");
        model.addAttribute("mailList",setMail);
        model.addAttribute("type",session.getAttribute("type"));
        model.addAttribute("type2",session.getAttribute("type2"));
        model.addAttribute("skill",session.getAttribute("skill"));
        //session.removeAttribute("skill");
        model.addAttribute("list",offeRequestList);

        model.addAttribute("offeRequest",session.getAttribute("offeRequest"));
        return "offeRequest/listexisting";
    }

    public String processConfirmAddSubmit(HttpSession session){
        OffeRequest offeRequest=(OffeRequest)session.getAttribute("offeRequest");
        offeRequestDao.add(offeRequest);
        //session.removeAttribute("offeRequest");
        session.setAttribute("correcto", true);
        return "redirect:list";
    }

    public String edit(Model model,String id,String type, HttpSession session){
        model.addAttribute("type",type);
        OffeRequest offeRequest= offeRequestDao.getOffeRequest(id);
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

        session.setAttribute("correcto", true);

        return "redirect:list";
    }

    public String processDelete(String idOffer, HttpSession session) {
        offeRequestDao.delete(idOffer);
        session.setAttribute("correcto", true);
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
